package com.nexo_wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.nexo_wallet.payloads.TransactionDto;
import com.nexo_wallet.service.TransactionService;
import com.nexo_wallet.service.impl.DownloadableStatementsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DownloadableStatementsService downloadableStatementsService;

    @PostMapping("/saveTransaction/{userName}")
    public ResponseEntity<Object> createTransaction(@PathVariable String userName, @RequestBody TransactionDto transactionDto) {
        String message = transactionService.saveTransaction(userName,transactionDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/filterTransaction")
    public ResponseEntity<List<TransactionDto>> filterTransaction(@RequestBody JSONObject jsonObject) {

        String userName=jsonObject.getString("userName");
        String cryptocurrency = jsonObject.getString("cryptocurrency");
        Date startDate = jsonObject.getDate("startDate");
        Date endDate = jsonObject.getDate("endDate");
        Date transactionDate = jsonObject.getDate("transactionDate");
        String transactionType = jsonObject.getString("transactionType");
        BigDecimal fiatValue = jsonObject.getBigDecimal("fiatValue");

        log.info("inside filterTransaction controller");
        log.info("transactionType :" + transactionType);
        log.info("cryptocurrency :" + cryptocurrency);
        log.info("fiatValue :" + fiatValue);

        List<TransactionDto> filteredTransactionList = transactionService.filterTransactions(userName, cryptocurrency, startDate, endDate, transactionDate, transactionType, fiatValue);

        return new ResponseEntity<>(filteredTransactionList, HttpStatus.OK);
    }

    @GetMapping("/searchTransaction")
    public ResponseEntity<Object> searchTransactionByUserName(@RequestBody JSONObject jsonObject) {
        Long transactionId = jsonObject.getLongValue("transactionId");
        TransactionDto transactionDto = transactionService.searchTransactionById(transactionId);
        return new ResponseEntity<>(transactionDto, HttpStatus.FOUND);
    }

    @GetMapping("/download/statement")
    public ResponseEntity<String> downloadStatement(
            @RequestParam String userName,
            @RequestParam String currencyName,
            @RequestParam String format) {

        List<Map<String, String>> fileContent = downloadableStatementsService.generateStatement(userName, currencyName, format);

        StringBuilder stringBuilder = new StringBuilder();
        // Append headers
        stringBuilder.append(String.format("%-25s", "Date"))
                .append(String.format("%-15s", "Type"))
                .append(String.format("%-15s", "Amount"))
                .append(String.format("%-15s", "Price"))
                .append(String.format("%-15s", "Total"))
                .append("\n");

        // Append transaction data
        for (Map<String, String> transaction : fileContent) {
            // Assuming the date string is stored in transaction.get("Date")
            String dateString = transaction.get("Date");

            // Parse the date string into a LocalDateTime object
            LocalDateTime dateTime = LocalDateTime.parse(dateString);

            // Format the LocalDateTime to display only the date part
            String formattedDate = dateTime.toLocalDate().toString();

            stringBuilder.append(String.format("%-25s", formattedDate))
                    .append(String.format("%-15s", transaction.get("Type")))
                    .append(String.format("%-15s", transaction.get("Amount")))
                    .append(String.format("%-15s", transaction.get("Price")))
                    .append(String.format("%-15s", transaction.get("Total")))
                    .append("\n");
        }

        /*HttpHeaders, which is a class representing HTTP headers.
        HttpHeaders allows us to set various headers for an HTTP response.*/
        HttpHeaders headers = new HttpHeaders();
        /*
          This line sets the content type of the HTTP response to plain text.
          Here, we are explicitly specifying that the response body contains plain text data.
         */
        headers.setContentType(MediaType.TEXT_PLAIN); // Set content type to plain text

        /*
           This line sets the Content-Disposition.
           The Content-Disposition header specifies the presentation style (inline or attachment) and the filename for the response.
           In this case, we are setting it to "attachment", indicating that the response should be treated as an attachment, prompting the user to download it.
           We concatenate "transaction_history." with the format parameter to create the filename. For example, if format is "csv", the filename will be "transaction_history.csv".
         */
        headers.setContentDispositionFormData("attachment", "transaction_history." + format);

        return ResponseEntity.ok()
                .headers(headers)
                .body(stringBuilder.toString());
    }


}
