package com.wallet.service.impl;

import com.bybit.api.client.domain.user.IsUta;
import com.bybit.api.client.domain.user.MemberType;
import com.bybit.api.client.domain.user.SwitchOption;
import com.bybit.api.client.domain.user.UserDataRequest;
import com.bybit.api.client.restApi.BybitApiUserRestClient;
import com.wallet.entities.SubUser;
import com.wallet.repositories.SubUserRepository;
import com.wallet.service.SubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class SubUserServiceImpl implements SubUserService {

    @Autowired
    public BybitApiUserRestClient bybitApiUserRestClient;

    @Autowired
    public SubUserRepository subUserRepository;

    @Override
    public Map<String, Object> createSubUserOnBybit(String userName, String password) {

        //var subUserRequest1 = UserDataRequest.builder().username("VictorWuTest3").password("Password123").memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
        UserDataRequest subUserRequest = UserDataRequest.builder().username(userName).password(password).memberType(MemberType.NORMAL_SUB_ACCOUNT).note("Some note").switchOption(SwitchOption.TURN_OFF).isUta(IsUta.CLASSIC_ACCOUNT).build();
        //var subUser1 = bybitApiUserRestClient.createSubMember(subUserRequest);
        Map<String, Object> subUser = (Map<String, Object>) bybitApiUserRestClient.createSubMember(subUserRequest);

        // Print the class name to understand what type of object is returned
        System.out.println("Returned object class: " + subUser.getClass().getName());

        // Print all entries in the map (optional, for debugging purposes)
        for (Map.Entry<String, Object> entry : subUser.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        String message = this.saveDetailsOfSubAccount(subUser);
        if(message.equals("subUser created and saved successfully!!")) {
            System.out.println("Message :"+message);
            System.out.println(subUser);
            return subUser;
        }
         return null;
    }

//    public String deleteSubUserOnBybit(String subMemberId)
//    {
//        //bybitApiUserRestClient
//    }

    public String saveDetailsOfSubAccount(Map<String, Object> subUser) {

        if (subUser.containsKey("result")) {
            Map<String, Object> result = (Map<String, Object>) subUser.get("result");

            if (result != null) {
                String userId = result.get("uid").toString();
                String userName = result.get("username").toString();
                int memberType = (int) result.get("memberType");
                int status = (int) result.get("status");
                String remark = result.get("remark").toString();

                SubUser subUserToSave=new SubUser(userId,userName,memberType,status,remark);
                this.subUserRepository.save(subUserToSave);
                return "subUser created and saved successfully!!";
            }

        }
        return "something went wrong!!";
    }
}

