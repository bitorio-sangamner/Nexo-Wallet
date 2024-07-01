package com.wallet.service.impl;

import com.bybit.api.client.domain.user.IsUta;
import com.bybit.api.client.domain.user.MemberType;
import com.bybit.api.client.domain.user.SwitchOption;
import com.bybit.api.client.domain.user.UserDataRequest;
import com.bybit.api.client.restApi.BybitApiUserRestClient;
import com.wallet.entities.SubUser;
import com.wallet.repositories.SubUserRepository;
import com.wallet.service.SubUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class SubUserServiceImpl implements SubUserService {

    @Autowired
    public BybitApiUserRestClient bybitApiUserRestClient;

    @Autowired
    public SubUserRepository subUserRepository;

    @Override
    public Map<String, Object> createSubUserOnBybit(String userName, String password) {

        //var subUserRequest1 = UserDataRequest.builder().username("VictorWuTest3").password("Password123").memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
        UserDataRequest subUserRequest = UserDataRequest.builder().username(userName).password(password).memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
         try {
             //var subUser1 = bybitApiUserRestClient.createSubMember(subUserRequest);
             Map<String, Object> subUserResponse = (Map<String, Object>) bybitApiUserRestClient.createSubMember(subUserRequest);
             var subUser = bybitApiUserRestClient.createSubMember(subUserRequest);

             // Print the class name to understand what type of object is returned
             System.out.println("Returned object class: " + subUserResponse.getClass().getName());
             System.out.println(subUserResponse);

             // Print all entries in the map (optional, for debugging purposes)
             for (Map.Entry<String, Object> entry : subUserResponse.entrySet()) {
                 System.out.println(entry.getKey() + " - " + entry.getValue());
             }

             if (subUserResponse.containsKey("result")) {
                 Map<String, Object> result = (Map<String, Object>) subUserResponse.get("result");
                 if (result != null && result.containsKey("uid") && result.containsKey("username") && result.containsKey("memberType") && result.containsKey("status") && result.containsKey("remark")) {
                     String message = this.saveDetailsOfSubAccount(result);
                     log.info("Message: {}", message);
                     if ("subUser created and saved successfully!!".equals(message)) {
                         return subUserResponse;
                     } else {
                         log.warn("Failed to save sub-user details: {}", message);
                         return null;
                     }
                 }
                 log.warn("Invalid result map, message: {}", subUserResponse.get("retMsg"));
                 return null;

             }
             log.warn("subUserResponse does not contain a result map.");
             return null;
         }
         catch (Exception e) {
             log.error("Error creating sub-user on Bybit", e);
             return null;
         }
    }

//    public String deleteSubUserOnBybit(String subMemberId)
//    {
//        //bybitApiUserRestClient
//    }

    public String saveDetailsOfSubAccount(Map<String, Object> result) {

        if (result == null) {
            return "Result map is null.";
        }
        try {
            String userId = result.get("uid").toString();
            String userName = result.get("username").toString();
            int memberType = (int) result.get("memberType");
            int status = (int) result.get("status");
            String remark = result.get("remark").toString();

            SubUser subUserToSave = new SubUser(userId, userName, memberType, status, remark);
            subUserRepository.save(subUserToSave);
            return "subUser created and saved successfully!!";
        } catch (Exception e) {
            log.error("Error saving sub-user details", e);
            return "Something went wrong while saving sub-user details.";
        }
    }
}

