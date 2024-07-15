package com.wallet.service.impl;

import com.bybit.api.client.domain.user.*;
import com.bybit.api.client.restApi.BybitApiUserRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.entities.SubUser;
import com.wallet.entities.SubUserApiKey;
import com.wallet.entities.UserWallet;
import com.wallet.payloads.SubUserDto;
import com.wallet.payloads.UserWalletDto;
import com.wallet.repositories.SubUserApiKeyRepository;
import com.wallet.repositories.SubUserRepository;
import com.wallet.service.SubUserService;
import com.wallet.util.JsonConverter;
import com.wallet.util.SubUserApiKeyRequest;
import com.wallet.util.SubUserApiKeyResponse;
import com.wallet.util.SubUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class SubUserServiceImpl implements SubUserService {

    @Autowired
    public BybitApiUserRestClient bybitApiUserRestClient;

    @Autowired
    public SubUserRepository subUserRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SubUserApiKeyRepository subUserApiKeyRepository;

    @Override
    public SubUserResponse createSubUserOnBybit(String userName, String password) {
        UserDataRequest subUserRequest = UserDataRequest.builder()
                .username(userName)
                .password(password)
                .memberType(MemberType.NORMAL_SUB_ACCOUNT)
                .note("Some note")
                .switchOption(SwitchOption.TURN_OFF)
                .isUta(IsUta.CLASSIC_ACCOUNT)
                .build();

        try {
            Map<String, Object> subUserMap = (Map<String, Object>) bybitApiUserRestClient.createSubMember(subUserRequest);
            // Convert LinkedHashMap to JSON string
            String jsonString = objectMapper.writeValueAsString(subUserMap);
            // Convert JSON string to SubUserResponse object using Jackson
            SubUserResponse subUserResponse = JsonConverter.convertJsonToJavaUsingJackson(jsonString, SubUserResponse.class);

            // Print the class name to understand what type of object is returned
            System.out.println("Returned object class: " + subUserResponse.getClass().getName());
            System.out.println(subUserResponse);

            if (subUserResponse != null && subUserResponse.getResult() != null) {
                SubUserResponse.Result result = subUserResponse.getResult();
                if (result != null && result.getUid() != null && result.getUsername() != null && result.getMemberType() != 0 && result.getStatus() != 0 && result.getRemark() != null) {
                    String message = this.saveDetailsOfSubAccount(result);
                    log.info("Message: {}", message);
                    if ("SubUser created and saved successfully!!".equals(message)) {
                        return subUserResponse;
                    } else {
                        log.warn("Failed to save sub-user details: {}", message);
                        return null;
                    }
                }
                log.warn("Invalid result map, message: {}", subUserResponse.getRetMsg());
                return null;
            }
            log.warn("subUserResponse does not contain a result map.");
            return null;
        } catch (Exception e) {
            log.error("Error creating sub-user on Bybit", e);
            return null;
        }
    }

    @Override
    public SubUserDto getSubUserByUserName(String userName) {

        SubUser subUser=subUserRepository.findByUsername(userName);
        return this.subUserToDto(subUser);
    }

    @Override
    public String createSubUserApiKey(SubUserApiKeyRequest subUserApiKeyRequest) throws Exception {

        log.info("inside createSubUserApiKey");
        log.info("subId :"+subUserApiKeyRequest.getSubuid()+" Note :"+subUserApiKeyRequest.getNote()+" Read only :"+subUserApiKeyRequest.getReadOnly());
        try {
            UserDataRequest subUserRequest = UserDataRequest.builder()
                    .subuid(subUserApiKeyRequest.getSubuid())
                    .note(subUserApiKeyRequest.getNote())
                    .readOnlyStatus(mapToReadOnlyStatus(subUserApiKeyRequest.getReadOnly()))
                    .userPermissionsMap(mapToUserPermissionsMap(subUserApiKeyRequest.getPermissions()))
                    .build();
            Map<String, Object> subUserMap = (Map<String, Object>) bybitApiUserRestClient.createSubAPI(subUserRequest);
            log.info("Sub user Api key response :"+subUserMap.toString());
            // Convert LinkedHashMap to JSON string
            String jsonString = objectMapper.writeValueAsString(subUserMap);
            // Convert JSON string to SubUserResponse object using Jackson
            SubUserApiKeyResponse subUserApiKeyResponse = JsonConverter.convertJsonToJavaUsingJackson(jsonString, SubUserApiKeyResponse.class);
            // Print the class name to understand what type of object is returned
            System.out.println("Returned object class: " + subUserApiKeyResponse.getClass().getName());
            System.out.println(subUserApiKeyResponse);

            if (subUserApiKeyResponse != null && subUserApiKeyResponse.getResult() != null) {
                SubUserApiKeyResponse.Result subUserApiKeyResult = subUserApiKeyResponse.getResult();
                if (subUserApiKeyResult != null && subUserApiKeyResult.getId() != null && subUserApiKeyResult.getApiKey() != null && subUserApiKeyResult.getSecret() != null) {
                    String message = saveDetailsOfSubUserApiKey(subUserApiKeyResult);
                    if ("SubUser ApiKey saved successfully!!".equals(message)) {
                        return "SubUser ApiKey created and saved successfully!!";
                    } else {
                        log.warn("Failed to save sub-user ApiKey: {}", message);
                        return "Failed to save sub-user ApiKey";
                    }
                }
                log.warn("Invalid result map, message: {}", subUserApiKeyResponse.getRetMsg());
                return "Invalid result map, message";
            }
            log.warn("subUserApiKeyResponse does not contain a result map.");
            return "subUserApiKeyResponse does not contain a result map.";
        }
        catch (Exception e) {
            log.error("Error creating sub-user ApiKey", e);
            return "Error creating sub-user ApiKey";
        }

    }

    public String saveDetailsOfSubUserApiKey(SubUserApiKeyResponse.Result subUserApiKeyResult)
    {
        try {
            subUserApiKeyRepository.save(SubUserApiKey.builder().id(Integer.parseInt(subUserApiKeyResult.getId()))
                    .apiKey(subUserApiKeyResult.getApiKey())
                    .secret(subUserApiKeyResult.getSecret())
                    .readOnly(subUserApiKeyResult.getReadOnly())
                    .note(subUserApiKeyResult.getNote())
                    .walletPermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getWallet()))))
                    .spotPermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getSpot()))))
                    .contractTradePermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getContractTrade()))))
                    .optionsPermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getOptions()))))
                    .copyTradingPermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getCopyTrading()))))
                    .blockTradePermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getBlockTrade()))))
                    .exchangePermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getExchange()))))
                    .nftPermissions(Collections.singletonList((String.valueOf(subUserApiKeyResult.getPermissions().getNFT()))))
                    .build());
            return "SubUser ApiKey saved successfully!!";
        }
        catch (Exception e) {
            log.error("Error saving sub-user ApiKey: {}", e.getMessage(), e);
            return "Something went wrong while saving sub-user ApiKey.";
        }
    }

    private ReadOnlyStatus mapToReadOnlyStatus(int value) {
        switch (value) {
            case 0:
                return ReadOnlyStatus.READ_AND_WRITE;
            case 1:
                return ReadOnlyStatus.READ_ONLY;
            default:
                throw new IllegalArgumentException("Invalid ReadOnlyStatus value: " + value);
        }
    }

    private UserPermissionsMap mapToUserPermissionsMap(SubUserApiKeyRequest.Permissions permissions) {
        Map<String, List<String>> permissionMap = new HashMap<>();

        if (permissions.getWallet() != null) {
            permissionMap.put("Wallet", permissions.getWallet());
        }
        if (permissions.getSpot() != null) {
            permissionMap.put("Spot", permissions.getSpot());
        }
        if (permissions.getContractTrade() != null) {
            permissionMap.put("ContractTrade", permissions.getContractTrade());
        }
        if (permissions.getOptions() != null) {
            permissionMap.put("Options", permissions.getOptions());
        }
        if (permissions.getExchange() != null) {
            permissionMap.put("Exchange", permissions.getExchange());
        }
        if (permissions.getCopyTrading() != null) {
            permissionMap.put("CopyTrading", permissions.getCopyTrading());
        }

        return UserPermissionsMap.builder()
                .permissionMap(permissionMap)
                .build();
    }


    /**
     * Saves the details of a sub-account.
     *
     * @param result The SubUserResponse.Result object containing the sub-account details.
     * @return A message indicating the result of the save operation.
     */
    public String saveDetailsOfSubAccount(SubUserResponse.Result result) {
        if (result == null) {
            return "Result map is null.";
        }

        try {
            // Extract details from the result object
            String userId = result.getUid();
            String userName = result.getUsername();
            int memberType = result.getMemberType();
            int status = result.getStatus();
            String remark = result.getRemark();

            // Create a new SubUser entity
            SubUser subUserToSave = new SubUser(userId, userName, memberType, status, remark);

            // Save the SubUser entity to the repository
            subUserRepository.save(subUserToSave);
            return "SubUser created and saved successfully!!";
        } catch (Exception e) {
            log.error("Error saving sub-user details: {}", e.getMessage(), e);
            return "Something went wrong while saving sub-user details.";
        }
    }


    public SubUserDto subUserToDto(SubUser subUser) {
        SubUserDto subUserDto=modelMapper.map(subUser, SubUserDto.class);
        return subUserDto;
    }

    public SubUser dtoToSubUser(SubUserDto subUserDto) {
        SubUser subUser=modelMapper.map(subUserDto, SubUser.class);
        return subUser;
    }
//    @Override
//    public Map<String, Object> createSubUserOnBybit(String userName, String password) {
//
//        //var subUserRequest1 = UserDataRequest.builder().username("VictorWuTest3").password("Password123").memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
//        UserDataRequest subUserRequest = UserDataRequest.builder().username(userName).password(password).memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
//         try {
//             //var subUser1 = bybitApiUserRestClient.createSubMember(subUserRequest);
//             Map<String, Object> subUserResponse = (Map<String, Object>) bybitApiUserRestClient.createSubMember(subUserRequest);
//
//
//             // Print the class name to understand what type of object is returned
//             System.out.println("Returned object class: " + subUserResponse.getClass().getName());
//             System.out.println(subUserResponse);
//
//             // Print all entries in the map (optional, for debugging purposes)
//             for (Map.Entry<String, Object> entry : subUserResponse.entrySet()) {
//                 System.out.println(entry.getKey() + " - " + entry.getValue());
//             }
//
//             if (subUserResponse.containsKey("result")) {
//                 Map<String, Object> result = (Map<String, Object>) subUserResponse.get("result");
//                 if (result != null && result.containsKey("uid") && result.containsKey("username") && result.containsKey("memberType") && result.containsKey("status") && result.containsKey("remark")) {
//                     String message = this.saveDetailsOfSubAccount(result);
//                     log.info("Message: {}", message);
//                     if ("subUser created and saved successfully!!".equals(message)) {
//                         return subUserResponse;
//                     } else {
//                         log.warn("Failed to save sub-user details: {}", message);
//                         return null;
//                     }
//                 }
//                 log.warn("Invalid result map, message: {}", subUserResponse.get("retMsg"));
//                 return null;
//
//             }
//             log.warn("subUserResponse does not contain a result map.");
//             return null;
//         }
//         catch (Exception e) {
//             log.error("Error creating sub-user on Bybit", e);
//             return null;
//         }
//    }
//
////    public String deleteSubUserOnBybit(String subMemberId)
////    {
////        //bybitApiUserRestClient
////    }
//
//    public String saveDetailsOfSubAccount(Map<String, Object> result) {
//
//        if (result == null) {
//            return "Result map is null.";
//        }
//        try {
//            String userId = result.get("uid").toString();
//            String userName = result.get("username").toString();
//            int memberType = (int) result.get("memberType");
//            int status = (int) result.get("status");
//            String remark = result.get("remark").toString();
//
//            SubUser subUserToSave = new SubUser(userId, userName, memberType, status, remark);
//            subUserRepository.save(subUserToSave);
//            return "subUser created and saved successfully!!";
//        } catch (Exception e) {
//            log.error("Error saving sub-user details", e);
//            return "Something went wrong while saving sub-user details.";
//        }
//    }
}

