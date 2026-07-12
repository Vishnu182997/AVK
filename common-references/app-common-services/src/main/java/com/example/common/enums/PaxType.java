package com.example.common.enums;

import com.example.common.util.AvkCommonUtil;

public enum PaxType {
    ADT, // Adult
    CH,
    CNN,
    CHD, // Child
    INF, // Infant
    INS, // Infant with a seat
    UNN; // unaccompanied child

    public static PaxType getPaxType(String paxType) {
		// Set Default Pax Type is ADT
    	if(!AvkCommonUtil.isNotNullOrEmpty(paxType)) {
    		return ADT;
    	}
        switch (paxType) {
            case "ADT":
            case "A":
            case "SEA":
                return ADT;
            case "CHD":
            case "CH":
            case "CNN":
                return CHD;
            case "INF":
            case "INFT":
            case "IN":
                return INF;
            case "INS":
                return INS;
            default:
                if(paxType.startsWith("C")){
                    return CHD;
                }else {
                    return UNN;
                }
        }

    }

    public static PaxType getPaxType(Integer paxType) {
        switch (paxType) {
            case 1:
                return ADT;
            case 2:
                return CHD;
            case 3:
                return INF;
            case 4:
                return INS;
            default:
                return UNN;
        }


    }

    public static Long getPaxTypeLong(String paxType) {
        switch (paxType) {
            case "ADT":
                return 1L;
            case "CHD":
            case "CH":
            case "CNN":
                return 2L;
            case "INF":
                return 3L;
            case "INS":
                return 4L;
            case "UNN":
                return 5L;
            default:
                return 1L;
        }
    }

    public static PaxType getGrncPaxType(String type) {
        if (type.equals("AD")) {
            return PaxType.ADT;
        } else if (type.equals("CH")) {
            return PaxType.CHD;
        } else {
            return null;
        }
    }
    
    public static String getHBSPaxType(String type) {
        if (type.equals("ADT")) {
            return "AD";
        } else if (type.equals("CHD")) {
            return "CH";
        } else {
            return null;
        }
    }
}
