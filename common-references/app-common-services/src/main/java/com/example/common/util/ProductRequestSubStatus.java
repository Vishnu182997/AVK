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
	public static final int PENDING_WITH_TRAVEL_AGENT= 41;
	public static final int PENDING_WITH_BOT = 42; //Auto Booking
	public static final int BOOKED_AWAITING_FOR_APPROVAL = 43;
	public static final int AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT = 44;
	public static final int CANCELLED_PROCESS_PAYMENT_PENDING = 45;
	public static final int PARTIALLY_CANCELLED = 46;
	public static final int AWAITING_FOR_RESCHEDULE_BY_TRAVEL_AGENT = 47;
	public static final int RESCHEDULED_PROCESS_PAYMENT_PENDING = 48;
	public static final int AWAITING_FOR_SPLIT_PNR_BY_TRAVEL_AGENT = 49;
	public static final int AWAITING_FOR_SSR_BY_TRAVEL_AGENT = 50;
	public static final int AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING = 51;
	public static final int PENDING_WITH_TRAVEL_AGENT_FOR_PROCESSING = 52;
	public static final int AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_TRAVEL_AGENT = 53;
	public static final int MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING = 54;
	public static final int PENDING_WITH_TRAVEL_AGENT_FOR_OPTION_SHARING = 55;
	public static final int PENDING_WITH_EMPLOYEE_FOR_OPTION_SELECTING = 56;
	public static final int PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_OPTIONS_SHARING = 57;
	public static final int BOOKING_PENDING_WITH_CORPORATE_TRAVEL_DESK = 58;
	public static final int PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_PROCESSING = 59;
	public static final int AWAITING_FOR_CANCELLATION_BY_EMPLOYEE = 60;
	public static final int AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK = 61;
	public static final int AWAITING_FOR_RESCHEDULE_BY_EMPLOYEE = 62;
	public static final int AWAITING_FOR_RESCHEDULE_BY_CORPORATE_TRAVEL_DESK = 63;
	public static final int AWAITING_FOR_SPLIT_PNR_BY_EMPLOYEE = 64;
	public static final int AWAITING_FOR_SPLIT_PNR_BY_CORPORATE_TRAVEL_DESK = 65;
	public static final int AWAITING_FOR_SSR_BY_EMPLOYEE = 66;
	public static final int AWAITING_FOR_SSR_BY_CORPORATE_TRAVEL_DESK = 67;
	public static final int AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_EMPLOYEE = 68;
	public static final int AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_CORPORATE_TRAVEL_DESK = 69;
	public static final int PENDING_WITH_EMPLOYEE_FOR_RESUBMISSION = 82;
	public static final int PENDING_WITH_TRAVEL_DESK_TO_ASSIGN_AGENT = 83;
	public static final int PENDING_WITH_CORPORATE_TRAVEL_DESK_TO_ASSIGN_AGENT = 84;
	public static final int PENDING_WITH_TRAVEL_DESK_FOR_APPROVAL = 85;
	public static final int PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_APPROVAL = 86;
	public static final int AWAITING_FOR_AMENDMENT_APPROVAL = 87;
	public static final int PENDING_WITH_TRAVEL_DESK_FOR_AMENDMENT_APPROVAL = 88;
	public static final int PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_AMENDMENT_APPROVAL = 89;
	public static final int RESCHEDULE_PENDING_WITH_TRAVEL_AGENT = 90;
	
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
    public static final String ASSIGN_REQUEST_WHEN_USER_DISABLED = "assignRequestWhenUserDisabled";
    public static final String IS_BID_SHARED_TO_ADMIN = "isBidSharedToAdmin";
    public static final String IS_BID_TO_ASSIGN = "isBidToAssign";
    public static final String IS_BID_MODULE = "isBidModule";
    public static final String IS_AI_CALL = "isAiCall";
    
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
		return FastCollabCommonUtil.hasData(prop.getProperty(subStatusId + "")) ? prop.getProperty(subStatusId + "")
				: statusDesc;
    }
    
    public static void main(String[] args) {
    	int a = 50;
    	int b = a++ + ++a;
    	//System.out.println(b);
//		System.out.println(getSubStatusDesc(99, "Pending with Employee for Submission"));
    	
    	Map<String, Object> statusParams = new HashMap<>();
		statusParams.put(ProductRequestSubStatus.CAN_BOOKED_BY_USER, true);
		statusParams.put(ProductRequestSubStatus.IS_APPROVAL_FLOW, false);
		statusParams.put(ProductRequestSubStatus.STATUS_ID, 4);
		//System.out.println(ProductRequestSubStatus.getSubStatus(statusParams, 9));
	}
    
	/**
	 * Determines the sub-status based on the current status ID, existing sub-status ID, approval flow, bot flow, and user booking capability.
	 *
	 * @param statusParams a map containing the input parameters
	 * @return the corresponding sub-status ID
	 * @throws IllegalArgumentException if the status ID is invalid
	 */
	public static Long getSubStatus(Map<String, Object> statusParams, Integer productType) {
		boolean isApprovalFlow = FastCollabCommonUtil.getBoolean(statusParams.get(IS_APPROVAL_FLOW));
		Long statusId = null;
	    if (isApprovalFlow) {
	         statusId = getApprovalFlowSubStatus(statusParams, productType);
	    } else {
	    	statusId = getDirectBookingFlowSubStatus(statusParams, productType);
	    }
	    //System.out.println("output Sub Status:: " + statusId);
	    return statusId;
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
		int statusId = FastCollabCommonUtil.getDefaultInteger(params.get(STATUS_ID));
		boolean isBotFlow = FastCollabCommonUtil.getBoolean(params.get(IS_BOT_FLOW));
		boolean isAiCall = FastCollabCommonUtil.getBoolean(params.get(IS_AI_CALL));
		boolean canBookedByUser = FastCollabCommonUtil.getBoolean(params.get(CAN_BOOKED_BY_USER));
		boolean isPostBookingApproval = FastCollabCommonUtil.getBoolean(params.get(IS_POST_BOOKING_APPROVAL_FLOW));
		Integer postBookingApprovalStatus = FastCollabCommonUtil.getDefaultInteger(params.get(POST_BOOKING_APPROVAL_STATUS));
		boolean isOffline = FastCollabCommonUtil.getBoolean(params.get(IS_OFFLINE));
		String headStatusDesc = FastCollabCommonUtil.getString(params.get(HEAD_STATUS_DESC));
		Integer assignRequestWhenUserDisabled = FastCollabCommonUtil.getDefaultInteger(params.get(ASSIGN_REQUEST_WHEN_USER_DISABLED));
		int existingSubStatusId = FastCollabCommonUtil.getDefaultInteger(params.get(EXISTING_SUB_STATUS_ID));

		//System.out.println("Map params:: " + FastCollabCommonUtil.getResponseBody(params));
		//System.out.println("existingSubStatusId:: " + existingSubStatusId);
		//System.out.println("statusId:: " + statusId);
		//System.out.println("headStatusDesc:: " + headStatusDesc);
		//System.out.println("assignRequestWhenUserDisabled:: " + assignRequestWhenUserDisabled);
		
		switch (statusId) {
		case 2:
			return statusId; // Awaiting for Approval
		case 4:
			if (productType == CABS || productType == RAIL || productType == VISA || productType == FOREX) {
				return assignRequestWhenUserDisabled == 1 ? PENDING_WITH_TRAVEL_AGENT_FOR_PROCESSING : PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_PROCESSING;
			} else {
				return ((isBotFlow || isAiCall) && !isOffline)
					    ? PENDING_WITH_BOT
					    : isOffline
					        ? (assignRequestWhenUserDisabled == 1
					            ? PENDING_WITH_TRAVEL_AGENT_FOR_OPTION_SHARING
					            : PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_OPTIONS_SHARING)
					        : canBookedByUser
					            ? PENDING_WITH_USER
					            : (assignRequestWhenUserDisabled == 1
					                ? PENDING_WITH_TRAVEL_AGENT
					                : BOOKING_PENDING_WITH_CORPORATE_TRAVEL_DESK); // Awaiting
																														// for
																														// Booking
			} // Awaiting for Booking
		case 37:
			return isBotFlow ? PENDING_WITH_BOT : PENDING_WITH_USER; // Booking In Progress
		case 15:
			return PENDING_CONFIRMATION_WITH_SUPPLIER; // PNR Cancelled
//	        case 14:
//	            return PARTIALLY_BOOKED; // PNR Blocked
		case 5:
			if (isPostBookingApproval) {
				if (postBookingApprovalStatus == 2) {
					return BOOKED_AWAITING_FOR_APPROVAL;
				} else if (postBookingApprovalStatus == 5) {
					return statusId; // Booked
				} else {
					return assignRequestWhenUserDisabled == 1 ? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT : AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK; // Awaiting for Cancellation;
				}
			}
			return statusId; // Booked
		case 6:
			return statusId; // Booking Failed
		case 11:
			return statusId; // Request Cancelled
		case 12:
			return FastCollabCommonUtil.hasData(headStatusDesc) 
					&& headStatusDesc.contains("Partially Booked") 
					? PARTIALLY_BOOKED : statusId;
		default:
			return statusId;
		}
	}

	/**
	 * Determines the sub-status for direct booking flow based on the current status
	 * ID, existing sub-status ID, and bot flow.
	 *
	 * @param params      a map containing the input parameters
	 * @param productType
	 * @return the corresponding sub-status ID
	 * @throws IllegalArgumentException if the status ID is invalid for direct
	 *                                  booking flow
	 */
	@SuppressWarnings("unused")
	private static long getDirectBookingFlowSubStatus(Map<String, Object> params, Integer productType) {
		int statusId = FastCollabCommonUtil.getDefaultInteger(params.get(STATUS_ID));
		int existingSubStatusId = FastCollabCommonUtil.getDefaultInteger(params.get(EXISTING_SUB_STATUS_ID));
		boolean isBotFlow = FastCollabCommonUtil.getBoolean(params.get(IS_BOT_FLOW));
		boolean isOffline = FastCollabCommonUtil.getBoolean(params.get(IS_OFFLINE));
		boolean canBookedByUser = FastCollabCommonUtil.getBoolean(params.get(CAN_BOOKED_BY_USER));
		String headStatusDesc = FastCollabCommonUtil.getString(params.get(HEAD_STATUS_DESC));
		boolean isCart = FastCollabCommonUtil.getBoolean(params.get(IS_CART));
		boolean isOptionShared = FastCollabCommonUtil.getBoolean(params.get(IS_OPTION_SHARED));
		boolean isPostBookingApproval = FastCollabCommonUtil.getBoolean(params.get(IS_POST_BOOKING_APPROVAL_FLOW));
		Integer postBookingApprovalStatus = FastCollabCommonUtil.getDefaultInteger(params.get(POST_BOOKING_APPROVAL_STATUS));
		boolean isForcedHide = FastCollabCommonUtil.getBoolean(params.get(IS_FORCED_HIDE));
		Integer assignRequestWhenUserDisabled = FastCollabCommonUtil.getDefaultInteger(params.get(ASSIGN_REQUEST_WHEN_USER_DISABLED));
		assignRequestWhenUserDisabled = assignRequestWhenUserDisabled == 0 ? 1: assignRequestWhenUserDisabled;
		boolean isBidSharedToAdmin =  FastCollabCommonUtil.getBoolean(params.get(IS_BID_SHARED_TO_ADMIN));				
		boolean isBidToAssign =  FastCollabCommonUtil.getBoolean(params.get(IS_BID_TO_ASSIGN));				
		boolean isBidModule =  FastCollabCommonUtil.getBoolean(params.get(IS_BID_MODULE));				
		boolean isAiCall = FastCollabCommonUtil.getBoolean(params.get(IS_AI_CALL));
		
		//System.out.println("isAiCall:: " + isAiCall);
		//System.out.println("Map params:: " + FastCollabCommonUtil.getResponseBody(params));
		//System.out.println("existingSubStatusId:: " + existingSubStatusId);
		//System.out.println("statusId:: " + statusId);
		//System.out.println("headStatusDesc:: " + headStatusDesc);
		//System.out.println("assignRequestWhenUserDisabled:: " + assignRequestWhenUserDisabled);
		
		switch (statusId) {
		case 4:
			if (productType == CABS || productType == RAIL || productType == VISA || productType == FOREX) {
				return assignRequestWhenUserDisabled == 1 ? PENDING_WITH_TRAVEL_AGENT_FOR_PROCESSING : PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_PROCESSING;
//	        	}else if(productType == FOREX){
//	        		return AWAITING_FOR_PROCESSING;
			} else {
				return ((isBotFlow || isAiCall) && !isOffline)
					    ? PENDING_WITH_BOT
					    : (isOffline
					        ? (isBidSharedToAdmin && existingSubStatusId == PENDING_WITH_TRAVEL_DESK_FOR_APPROVAL
					            ? (assignRequestWhenUserDisabled == 1 ? PENDING_WITH_TRAVEL_DESK_FOR_APPROVAL : PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_APPROVAL)
					            : (isBidToAssign && existingSubStatusId == PENDING_WITH_TRAVEL_DESK_TO_ASSIGN_AGENT ? (assignRequestWhenUserDisabled == 1 ? PENDING_WITH_TRAVEL_DESK_TO_ASSIGN_AGENT : PENDING_WITH_CORPORATE_TRAVEL_DESK_TO_ASSIGN_AGENT) : (assignRequestWhenUserDisabled == 1
					                    ? PENDING_WITH_TRAVEL_AGENT_FOR_OPTION_SHARING
					                    : PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_OPTIONS_SHARING)))
					        : (canBookedByUser
					            ? PENDING_WITH_USER
					            : (assignRequestWhenUserDisabled == 1
					                ? PENDING_WITH_TRAVEL_AGENT
					                : BOOKING_PENDING_WITH_CORPORATE_TRAVEL_DESK))); // Awaiting
																														// for
																														// Booking
			}
		case 37:
			if (isBotFlow)
				return PENDING_WITH_BOT; // Booking In Progress with Bot
			return existingSubStatusId == PENDING_WITH_TRAVEL_AGENT ? PENDING_WITH_TRAVEL_AGENT
					: PENDING_WITH_USER; // Booking
																													// In
																													// Progress
//	        case 14:
//	            return PARTIALLY_BOOKED; // PNR Blocked
		case 15:
			return PENDING_CONFIRMATION_WITH_SUPPLIER; // PNR Cancelled
		case 5:
			if (isPostBookingApproval) {
				if (postBookingApprovalStatus == 2) {
					return BOOKED_AWAITING_FOR_APPROVAL;
				} else if (postBookingApprovalStatus == 5) {
					return statusId; // Booked
				} else {
					return assignRequestWhenUserDisabled == 1 
					   		? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT 
					   		: AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK;
					/*
					return canBookedByUser ? AWAITING_FOR_CANCELLATION_BY_EMPLOYEE
										   : (assignRequestWhenUserDisabled == 1 
										   		? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT 
										   		: AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK); // Awaiting for Cancellation;
					*/
				}
			}
			//System.out.println("IS COND TRUE::" + (existingSubStatusId == RESCHEDULED_PROCESS_PAYMENT_PENDING && headStatusDesc.equalsIgnoreCase("Re-Booking Confirmed (Process Payment Pending)")));
			if (existingSubStatusId == RESCHEDULED_PROCESS_PAYMENT_PENDING
					&& headStatusDesc.equalsIgnoreCase("Re-Booking Confirmed (Process Payment Pending)")) {
				return RESCHEDULED_PROCESS_PAYMENT_PENDING;
			}
			return statusId; // Booked
		case 6:
			return statusId; // Booking Failed
		case 11:
			return statusId; // Request Cancelled
		case 1:
			return productType == 21 && FastCollabCommonUtil.hasData(headStatusDesc)
					&& headStatusDesc.equalsIgnoreCase("Empty Trip")
							? EMPTY_TRIP
							: (productType == 21 ? PENDING_WITH_EMPLOYEE_FOR_ADDING_SERVICES
									: (isForcedHide ? 
											(FastCollabCommonUtil.hasData(existingSubStatusId) && existingSubStatusId == 82 ? 
													existingSubStatusId :  statusId )// Pending with Employee for Submission
											: (isOffline 
													? 
														(assignRequestWhenUserDisabled == 1 
														?  PENDING_WITH_TRAVEL_AGENT_FOR_OPTION_SHARING 
														: PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_OPTIONS_SHARING) 
													: PENDING_WITH_TRAVEL_AGENT)));

		case 8:
			return FastCollabCommonUtil.hasData(headStatusDesc) && headStatusDesc.contains("Process Payment")
					? CANCELLED_PROCESS_PAYMENT_PENDING
					: statusId; // Booking Cancelled
		case 9:
			//System.out.println("Existing Sub Status Id:: " + existingSubStatusId);
			if (FastCollabCommonUtil.hasData(existingSubStatusId)) {
				switch (existingSubStatusId) {
				case AWAITING_FOR_RESCHEDULE_BY_TRAVEL_AGENT:
				case AWAITING_FOR_RESCHEDULE_BY_EMPLOYEE:
					return assignRequestWhenUserDisabled == 1 ? AWAITING_FOR_RESCHEDULE_BY_TRAVEL_AGENT
							: AWAITING_FOR_RESCHEDULE_BY_CORPORATE_TRAVEL_DESK;
					/*
					return canBookedByUser ? AWAITING_FOR_RESCHEDULE_BY_EMPLOYEE 
										   : (assignRequestWhenUserDisabled == 1 
										   		? AWAITING_FOR_RESCHEDULE_BY_TRAVEL_AGENT 
										   		: AWAITING_FOR_RESCHEDULE_BY_CORPORATE_TRAVEL_DESK);
										   		*/
				case RESCHEDULED_PROCESS_PAYMENT_PENDING:
					return RESCHEDULED_PROCESS_PAYMENT_PENDING;
				case AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING:
					return AWAITING_FOR_SSR_PROCESS_PAYMENT_PENDING;
				case MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING:
					return MISCELLANEOUS_REQUEST_PROCESS_PAYMENT_PENDING;
				case AWAITING_FOR_AMENDMENT_APPROVAL:
					return AWAITING_FOR_AMENDMENT_APPROVAL;
				case PENDING_WITH_TRAVEL_DESK_FOR_AMENDMENT_APPROVAL:
					return assignRequestWhenUserDisabled == 1 
					   		? PENDING_WITH_TRAVEL_DESK_FOR_AMENDMENT_APPROVAL 
					   		: PENDING_WITH_CORPORATE_TRAVEL_DESK_FOR_AMENDMENT_APPROVAL;
				}
			}
			//System.out.println("Existing Sub Status Id:: " + existingSubStatusId);
			if (FastCollabCommonUtil.hasData(headStatusDesc)) {
				if (headStatusDesc.contains("Process Payment")) {
					return CANCELLED_PROCESS_PAYMENT_PENDING;
				} else if (headStatusDesc.equalsIgnoreCase("Awaiting for Split PNR")) {
					/*
					return canBookedByUser ? AWAITING_FOR_SPLIT_PNR_BY_EMPLOYEE 
										   : (assignRequestWhenUserDisabled == 1 
										   		? AWAITING_FOR_SPLIT_PNR_BY_TRAVEL_AGENT 
										   		: AWAITING_FOR_SPLIT_PNR_BY_CORPORATE_TRAVEL_DESK);
					*/
					return assignRequestWhenUserDisabled == 1 
					   		? AWAITING_FOR_SPLIT_PNR_BY_TRAVEL_AGENT 
					   		: AWAITING_FOR_SPLIT_PNR_BY_CORPORATE_TRAVEL_DESK;
				} else if (headStatusDesc.equalsIgnoreCase("Awaiting for SSR")) {
					return assignRequestWhenUserDisabled == 1 
					   		? AWAITING_FOR_SSR_BY_TRAVEL_AGENT 
					   		: AWAITING_FOR_SSR_BY_CORPORATE_TRAVEL_DESK;
					/*
					return canBookedByUser ? AWAITING_FOR_SSR_BY_EMPLOYEE 
										   : (assignRequestWhenUserDisabled == 1 
										   		? AWAITING_FOR_SSR_BY_TRAVEL_AGENT 
										   		: AWAITING_FOR_SSR_BY_CORPORATE_TRAVEL_DESK);
					*/
				} else if (headStatusDesc.equalsIgnoreCase("Awaiting for Miscellaneous Request")) {
					return assignRequestWhenUserDisabled == 1 
					   		? AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_TRAVEL_AGENT 
					   		: AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_CORPORATE_TRAVEL_DESK;
					/*
					return canBookedByUser ? AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_EMPLOYEE 
										   : (assignRequestWhenUserDisabled == 1 
										   		? AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_TRAVEL_AGENT 
										   		: AWAITING_FOR_MISCELLANEOUS_REQUEST_BY_CORPORATE_TRAVEL_DESK);
					*/
				}
			}
			if(productType == CABS || productType == RAIL || productType == VISA || productType == FOREX) {
				return assignRequestWhenUserDisabled == 1 
				   		? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT 
				   		: AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK;
			}
			/*
			return canBookedByUser ? AWAITING_FOR_CANCELLATION_BY_EMPLOYEE 
								   : (assignRequestWhenUserDisabled == 1 
								   		? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT 
								   		: AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK);
			*/
			return (assignRequestWhenUserDisabled == 1 || isBidModule) ? AWAITING_FOR_CANCELLATION_BY_TRAVEL_AGENT
					: AWAITING_FOR_CANCELLATION_BY_CORPORATE_TRAVEL_DESK;

		case 10:
			return isOptionShared ? PENDING_WITH_EMPLOYEE_FOR_OPTION_SELECTING : statusId;
		case 12:
			return FastCollabCommonUtil.hasData(headStatusDesc) 
					&& headStatusDesc.contains("Partially Booked")
					? PARTIALLY_BOOKED : statusId;
		default:
			return statusId;
		}
	}
}
