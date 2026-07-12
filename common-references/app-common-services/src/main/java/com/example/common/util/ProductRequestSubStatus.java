package com.example.common.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.example.common.logger.LogUtils;

public class ProductRequestSubStatus {

	public static final int PENDING_WITH_EMPLOYEE_FOR_ADDING_SERVICES = 0;
	public static final int PENDING_WITH_EMPLOYEE_FOR_SUBMISSION = 1;
	public static final int AWAITING_FOR_PROCESSING = 36;
	public static final int PENDING_CONFIRMATION_WITH_SUPPLIER = 38;
	public static final int PARTIALLY_BOOKED = 39;
	public static final int PENDING_WITH_USER = 40;
	public static final int PENDING_WITH_CONSULTANT = 41;
	public static final int PENDING_WITH_BOT = 42; //Auto Booking
	public static final int BOOKED_AWAITING_FOR_APPROVAL = 43;
	public static final int AWAITING_FOR_CANCELLATION = 44;
	public static final int CANCELLED_PROCESS_PAYMENT_PENDING = 45;
	public static final int PARTIALLY_CANCELLED = 46;
	public static final int AWAITING_FOR_RESCHEDULE = 47;
	public static final int RESCHEDULED_PROCESS_PAYMENT_PENDING = 48;
	public static final int AWAITING_FOR_SPLIT_PNR = 49;
	public static final int AWAITING_FOR_SSR = 50;
	public static final int AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING = 51;
	public static final int PENDING_WITH_CONSULTANT_FOR_PROCESSING = 52;
	public static final int AWAITING_FOR_MISCELLANEOUS_REQUEST = 53;
	public static final int MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING = 54;
	public static final int PENDING_WITH_CONSULTANT_FOR_OPTION_SHARING = 55;
	public static final int PENDING_WITH_EMPLOYEE_FOR_OPTION_SELECTING = 56;
	public static final int EMPTY_TRIP = 99;
	
	
	// Static variables for map keys
    public static final String STATUS_ID = "statusId";
    public static final String EXISTING_SUB_STATUS_ID = "existingSubStatusId";
    public static final String IS_APPROVAL_FLOW = "isApprovalFlow";
    public static final String IS_POST_BOOKING_APPROVAL_FLOW = "isPostBookingApprovalFlow";
    public static final String IS_BOT_FLOW = "isBotFlow";
    public static final String CAN_BOOKED_BY_USER = "canBookedByUser";
    public static final String IS_OFFLINE = "isOffline";
    public static final String HEAD_STATUS_DESC = "headStatusDesc";
    public static final String IS_CART = "isCart";
    public static final String IS_OPTION_SHARED = "isOptionShared";
    public static final String POST_BOOKING_APPROVAL_STATUS = "postBookingApprovalStatus";
    public static final String IS_SUBMIT_FLOW = "isSubmitFlow";
    public static final String IS_FORCED_HIDE = "isForcedHide";
    
    // Static variables for product types
    public static final int FLIGHT = 1;
    public static final int HOTEL = 2;
    public static final int VISA = 3;
    public static final int FOREX = 4;
    public static final int CALLING_CARD = 5;
    public static final int CABS = 6;
    public static final int INSURANCE = 7;
    public static final int BUS = 8;
    public static final int RAIL = 9;
    
    public static Properties prop = new Properties();
    
    static {
        InputStream resourceAsStream = ProductRequestSubStatus.class.getClassLoader().getResourceAsStream("request_status.properties");
        try {
            prop.load(resourceAsStream);
        } catch (Exception e) {
        	LogUtils.printDebuggerLogs(null, "Failed to load Request Status properties: " + e, null,null);
            throw new RuntimeException("Failed to load Request Status properties", e);
        }
    }
    
    public static String getSubStatusDesc(Integer subStatusId, String statusDesc) {
		return AvkCommonUtil.hasData(prop.getProperty(subStatusId + "")) ? prop.getProperty(subStatusId + "")
				: statusDesc;
    }
    
    public static void main(String[] args) {
//		System.out.println(getSubStatusDesc(99, "Pending with Employee for Submission"));
    	
    	Map<String, Object> statusParams = new HashMap<>();
		statusParams.put(ProductRequestSubStatus.CAN_BOOKED_BY_USER, true);
		statusParams.put(ProductRequestSubStatus.IS_APPROVAL_FLOW, false);
		statusParams.put(ProductRequestSubStatus.STATUS_ID, 4);
		System.out.println(ProductRequestSubStatus.getSubStatus(statusParams, 9));
	}
    
	/**
	 * Determines the sub-status based on the current status ID, existing sub-status ID, approval flow, bot flow, and user booking capability.
	 *
	 * @param statusParams a map containing the input parameters
	 * @return the corresponding sub-status ID
	 * @throws IllegalArgumentException if the status ID is invalid
	 */
	public static Long getSubStatus(Map<String, Object> statusParams, Integer productType) {
		boolean isApprovalFlow = AvkCommonUtil.getBoolean(statusParams.get(IS_APPROVAL_FLOW));

	    if (isApprovalFlow) {
	        return getApprovalFlowSubStatus(statusParams, productType);
	    } else {
	        return getDirectBookingFlowSubStatus(statusParams, productType);
	    }
	}

	/**
	 * Determines the sub-status for approval flow based on the current status ID, bot flow, and user booking capability.
	 *
	 * @param params a map containing the input parameters
	 * @param productType 
	 * @return the corresponding sub-status ID
	 * @throws IllegalArgumentException if the status ID is invalid for approval flow
	 */
	private static long getApprovalFlowSubStatus(Map<String, Object> params, Integer productType) {
		 int statusId = AvkCommonUtil.getDefaultInteger(params.get(STATUS_ID));
	     boolean isBotFlow = AvkCommonUtil.getBoolean(params.get(IS_BOT_FLOW));
	     boolean canBookedByUser = AvkCommonUtil.getBoolean(params.get(CAN_BOOKED_BY_USER));
	     boolean isPostBookingApproval = AvkCommonUtil.getBoolean(params.get(IS_POST_BOOKING_APPROVAL_FLOW));
	     Integer postBookingApprovalStatus = AvkCommonUtil.getDefaultInteger(params.get(POST_BOOKING_APPROVAL_STATUS));
//	     boolean isOffline = FastCollabCommonUtil.getBoolean(params.get(IS_OFFLINE));

	    switch (statusId) {
	        case 2:
	            return statusId; // Awaiting for Approval
	        case 4:
	        	if(productType == CABS || productType == RAIL) {
	        		return PENDING_WITH_CONSULTANT_FOR_PROCESSING;
	        	}else if(productType == FOREX){
	        		return AWAITING_FOR_PROCESSING;
	        	} else {
	        		return isBotFlow ? PENDING_WITH_BOT : (canBookedByUser ? PENDING_WITH_USER : PENDING_WITH_CONSULTANT) ; // Awaiting for Booking
	        	}// Awaiting for Booking
	        case 37:
	            return isBotFlow ? PENDING_WITH_BOT : PENDING_WITH_USER; // Booking In Progress
	        case 15:
	            return PENDING_CONFIRMATION_WITH_SUPPLIER; // PNR Cancelled
	        case 14:
	            return PARTIALLY_BOOKED; // PNR Blocked
	        case 5:
				if (isPostBookingApproval) {
					if (postBookingApprovalStatus == 2) {
						return BOOKED_AWAITING_FOR_APPROVAL;
					} else if (postBookingApprovalStatus == 5) {
						return statusId; // Booked
					} else {
						return AWAITING_FOR_CANCELLATION; // Awaiting for Cancellation;
					}
				}
	        	return statusId; // Booked
	        case 6:
	            return statusId; // Booking Failed
	        case 11:
	            return statusId; // Request Cancelled
	        default:
	            return statusId;
	    }
	}

	/**
	 * Determines the sub-status for direct booking flow based on the current status ID, existing sub-status ID, and bot flow.
	 *
	 * @param params a map containing the input parameters
	 * @param productType 
	 * @return the corresponding sub-status ID
	 * @throws IllegalArgumentException if the status ID is invalid for direct booking flow
	 */
	@SuppressWarnings("unused")
	private static long getDirectBookingFlowSubStatus(Map<String, Object> params, Integer productType) {
		int statusId = AvkCommonUtil.getDefaultInteger(params.get(STATUS_ID));
        int existingSubStatusId = AvkCommonUtil.getDefaultInteger(params.get(EXISTING_SUB_STATUS_ID));
        boolean isBotFlow = AvkCommonUtil.getBoolean(params.get(IS_BOT_FLOW));
        boolean isOffline = AvkCommonUtil.getBoolean(params.get(IS_OFFLINE));
        boolean canBookedByUser = AvkCommonUtil.getBoolean(params.get(CAN_BOOKED_BY_USER));
        String headStatusDesc = AvkCommonUtil.getString(params.get(HEAD_STATUS_DESC));
        boolean isCart = AvkCommonUtil.getBoolean(params.get(IS_CART));
        boolean isOptionShared = AvkCommonUtil.getBoolean(params.get(IS_OPTION_SHARED)); 
        boolean isPostBookingApproval = AvkCommonUtil.getBoolean(params.get(IS_POST_BOOKING_APPROVAL_FLOW));
	    Integer postBookingApprovalStatus = AvkCommonUtil.getDefaultInteger(params.get(POST_BOOKING_APPROVAL_STATUS));
	    boolean isForcedHide = AvkCommonUtil.getBoolean(params.get(IS_FORCED_HIDE));
	    
	    System.out.println("existingSubStatusId:: "+ existingSubStatusId);
	    System.out.println("statusId:: "+ statusId);
	    System.out.println("headStatusDesc:: "+ headStatusDesc);
	    switch (statusId) { 
	        case 4:
	        	if(productType == CABS || productType == RAIL) {
	        		return PENDING_WITH_CONSULTANT_FOR_PROCESSING;
	        	}else if(productType == FOREX){
	        		return AWAITING_FOR_PROCESSING;
	        	} else {
	        		return isBotFlow ? PENDING_WITH_BOT : (canBookedByUser ? PENDING_WITH_USER : PENDING_WITH_CONSULTANT) ; // Awaiting for Booking
	        	}
	        case 37:
	            if (isBotFlow) return PENDING_WITH_BOT; // Booking In Progress with Bot
	            return existingSubStatusId == PENDING_WITH_CONSULTANT ? PENDING_WITH_CONSULTANT : PENDING_WITH_USER; // Booking In Progress
	        case 14:
	            return PARTIALLY_BOOKED; // PNR Blocked
	        case 15:
	            return PENDING_CONFIRMATION_WITH_SUPPLIER; // PNR Cancelled
	        case 5:
				if (isPostBookingApproval) {
					if (postBookingApprovalStatus == 2) {
						return BOOKED_AWAITING_FOR_APPROVAL;
					} else if (postBookingApprovalStatus == 5) {
						return statusId; // Booked
					} else {
						return AWAITING_FOR_CANCELLATION; // Awaiting for Cancellation;
					}
				}
				System.out.println("IS COND TRUE::"+ (existingSubStatusId == RESCHEDULED_PROCESS_PAYMENT_PENDING && headStatusDesc.equalsIgnoreCase("Re-Booking Confirmed (Process Payment Pending)")));
				if(existingSubStatusId == RESCHEDULED_PROCESS_PAYMENT_PENDING && headStatusDesc.equalsIgnoreCase("Re-Booking Confirmed (Process Payment Pending)")) {
					return RESCHEDULED_PROCESS_PAYMENT_PENDING;
				}
	        	return statusId; // Booked
	        case 6:
	            return statusId; // Booking Failed
	        case 11:
	            return statusId; // Request Cancelled
	        case 1:
	            return productType==21 && AvkCommonUtil.hasData(headStatusDesc) 
	            		&& headStatusDesc.equalsIgnoreCase("Empty Trip") 
	            			? EMPTY_TRIP 
        					: (productType==21 
	            					? PENDING_WITH_EMPLOYEE_FOR_ADDING_SERVICES 
	            					: (isForcedHide 
	            							? statusId // Pending with Employee for Submission 
	            							: (isOffline 
	            									? PENDING_WITH_CONSULTANT_FOR_OPTION_SHARING  
	            									: PENDING_WITH_CONSULTANT)));        
	        case 8:
	        	return AvkCommonUtil.hasData(headStatusDesc) && headStatusDesc.contains("Process Payment") ? CANCELLED_PROCESS_PAYMENT_PENDING : statusId; // Booking Cancelled
	        case 9:
	        	if(AvkCommonUtil.hasData(existingSubStatusId)) {
	        		switch (existingSubStatusId) {
					case AWAITING_FOR_RESCHEDULE:
						return AWAITING_FOR_RESCHEDULE;
					case RESCHEDULED_PROCESS_PAYMENT_PENDING:
						return RESCHEDULED_PROCESS_PAYMENT_PENDING;
					case AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING:
						return AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING;
					case MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING:
						return MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING;
	        		}
	        	} 
	        	if(AvkCommonUtil.hasData(headStatusDesc)) {
	        		if(headStatusDesc.contains("Process Payment")) {
	        			return CANCELLED_PROCESS_PAYMENT_PENDING;
	        		}else if(headStatusDesc.equalsIgnoreCase("Awaiting for Split PNR")) {
	        			return AWAITING_FOR_SPLIT_PNR;
	        		}else if(headStatusDesc.equalsIgnoreCase("Awaiting for SSR")) {
	        			return AWAITING_FOR_SSR;
	        		}else if(headStatusDesc.equalsIgnoreCase("Awaiting for Miscellaneous Request")) {
	        			return AWAITING_FOR_MISCELLANEOUS_REQUEST;
	        		}  
	        	} 
        		return AWAITING_FOR_CANCELLATION;
        		 
	        case 10:
	        	return isOptionShared ? PENDING_WITH_EMPLOYEE_FOR_OPTION_SELECTING : statusId;	
	        default:
	            return statusId;
	    }
	}
}
