package com.example.logger.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;


@NoArgsConstructor
@Getter
@Setter
public class ThirdPartyLoggerFactory {

    private static String lambda_Name;
    private static String resource_Path;


    /**
     * @param lambda_Name
     * @param resource_Path
     */

    public static void createThirdPartyLoggerFactory(String lambda_Name, String resource_Path) {
        ThirdPartyLoggerFactory.lambda_Name = lambda_Name;
        ThirdPartyLoggerFactory.resource_Path = resource_Path;
    }

    /**
     * @return ThirdPartyLoggerDTO
     */
    public static ThirdPartyLoggerDTO getThirdPartyLoggerDTO() {
        return ThirdPartyLoggerDTO.builder()
                .lambda_Name(ThirdPartyLoggerFactory.lambda_Name)
                .resource_Path(ThirdPartyLoggerFactory.resource_Path)
                .key(String.valueOf(Calendar.getInstance().getTimeInMillis()))
                .build();
    }
}

