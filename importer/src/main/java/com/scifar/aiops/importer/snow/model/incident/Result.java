
package com.scifar.aiops.importer.snow.model.incident;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "parent",
    "made_sla",
    "caused_by",
    "watch_list",
    "upon_reject",
    "sys_updated_on",
    "child_incidents",
    "hold_reason",
    "approval_history",
    "number",
    "resolved_by",
    "sys_updated_by",
    "opened_by",
    "user_input",
    "sys_created_on",
    "sys_domain",
    "state",
    "sys_created_by",
    "knowledge",
    "order",
    "calendar_stc",
    "closed_at",
    "cmdb_ci",
    "delivery_plan",
    "impact",
    "active",
    "work_notes_list",
    "business_service",
    "priority",
    "sys_domain_path",
    "rfc",
    "time_worked",
    "expected_start",
    "opened_at",
    "business_duration",
    "group_list",
    "work_end",
    "caller_id",
    "reopened_time",
    "resolved_at",
    "approval_set",
    "subcategory",
    "work_notes",
    "short_description",
    "close_code",
    "correlation_display",
    "delivery_task",
    "work_start",
    "assignment_group",
    "additional_assignee_list",
    "business_stc",
    "description",
    "calendar_duration",
    "close_notes",
    "notify",
    "sys_class_name",
    "closed_by",
    "follow_up",
    "parent_incident",
    "sys_id",
    "contact_type",
    "reopened_by",
    "incident_state",
    "urgency",
    "problem_id",
    "company",
    "reassignment_count",
    "activity_due",
    "assigned_to",
    "severity",
    "comments",
    "approval",
    "sla_due",
    "comments_and_work_notes",
    "due_date",
    "sys_mod_count",
    "reopen_count",
    "sys_tags",
    "escalation",
    "upon_approval",
    "correlation_id",
    "location",
    "category"
})
public class Result {

    @JsonProperty("parent")
    private String parent;
    @JsonProperty("made_sla")
    private String madeSla;
    @JsonProperty("caused_by")
    private String causedBy;
    @JsonProperty("watch_list")
    private String watchList;
    @JsonProperty("upon_reject")
    private String uponReject;
    @JsonProperty("sys_updated_on")
    private String sysUpdatedOn;
    @JsonProperty("child_incidents")
    private String childIncidents;
    @JsonProperty("hold_reason")
    private String holdReason;
    @JsonProperty("approval_history")
    private String approvalHistory;
    @JsonProperty("number")
    private String number;
    @JsonProperty("resolved_by")
    private LinkValue resolvedBy;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("opened_by")
    private LinkValue openedBy;
    @JsonProperty("user_input")
    private String userInput;
    @JsonProperty("sys_created_on")
    private String sysCreatedOn;
    @JsonProperty("sys_domain")
    private LinkValue sysDomain;
    @JsonProperty("state")
    private String state;
    @JsonProperty("sys_created_by")
    private String sysCreatedBy;
    @JsonProperty("knowledge")
    private String knowledge;
    @JsonProperty("order")
    private String order;
    @JsonProperty("calendar_stc")
    private String calendarStc;
    @JsonProperty("closed_at")
    private String closedAt;
    @JsonProperty("cmdb_ci")
    private LinkValue cmdbCi;
    @JsonProperty("delivery_plan")
    private String deliveryPlan;
    @JsonProperty("impact")
    private String impact;
    @JsonProperty("active")
    private String active;
    @JsonProperty("work_notes_list")
    private String workNotesList;
    @JsonProperty("business_service")
    private LinkValue businessService;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("sys_domain_path")
    private String sysDomainPath;
    @JsonProperty("rfc")
    private String rfc;
    @JsonProperty("time_worked")
    private String timeWorked;
    @JsonProperty("expected_start")
    private String expectedStart;
    @JsonProperty("opened_at")
    private String openedAt;
    @JsonProperty("business_duration")
    private String businessDuration;
    @JsonProperty("group_list")
    private String groupList;
    @JsonProperty("work_end")
    private String workEnd;
    @JsonProperty("caller_id")
    private LinkValue callerId;
    @JsonProperty("reopened_time")
    private String reopenedTime;
    @JsonProperty("resolved_at")
    private String resolvedAt;
    @JsonProperty("approval_set")
    private String approvalSet;
    @JsonProperty("subcategory")
    private String subcategory;
    @JsonProperty("work_notes")
    private String workNotes;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("close_code")
    private String closeCode;
    @JsonProperty("correlation_display")
    private String correlationDisplay;
    @JsonProperty("delivery_task")
    private String deliveryTask;
    @JsonProperty("work_start")
    private String workStart;
    @JsonProperty("assignment_group")
    private LinkValue assignmentGroup;
    @JsonProperty("additional_assignee_list")
    private String additionalAssigneeList;
    @JsonProperty("business_stc")
    private String businessStc;
    @JsonProperty("description")
    private String description;
    @JsonProperty("calendar_duration")
    private String calendarDuration;
    @JsonProperty("close_notes")
    private String closeNotes;
    @JsonProperty("notify")
    private String notify;
    @JsonProperty("sys_class_name")
    private String sysClassName;
    @JsonProperty("closed_by")
    private LinkValue closedBy;
    @JsonProperty("follow_up")
    private String followUp;
    @JsonProperty("parent_incident")
    private LinkValue parentIncident;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("contact_type")
    private String contactType;
    @JsonProperty("reopened_by")
    private String reopenedBy;
    @JsonProperty("incident_state")
    private String incidentState;
    @JsonProperty("urgency")
    private String urgency;
    @JsonProperty("problem_id")
    private LinkValue problemId;
    @JsonProperty("company")
    private LinkValue company;
    @JsonProperty("reassignment_count")
    private String reassignmentCount;
    @JsonProperty("activity_due")
    private String activityDue;
    @JsonProperty("assigned_to")
    private LinkValue assignedTo;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("comments")
    private String comments;
    @JsonProperty("approval")
    private String approval;
    @JsonProperty("sla_due")
    private String slaDue;
    @JsonProperty("comments_and_work_notes")
    private String commentsAndWorkNotes;
    @JsonProperty("due_date")
    private String dueDate;
    @JsonProperty("sys_mod_count")
    private String sysModCount;
    @JsonProperty("reopen_count")
    private String reopenCount;
    @JsonProperty("sys_tags")
    private String sysTags;
    @JsonProperty("escalation")
    private String escalation;
    @JsonProperty("upon_approval")
    private String uponApproval;
    @JsonProperty("correlation_id")
    private String correlationId;
    @JsonProperty("location")
    private LinkValue location;
    @JsonProperty("category")
    private String category;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("parent")
    public String getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(String parent) {
        this.parent = parent;
    }

    @JsonProperty("made_sla")
    public String getMadeSla() {
        return madeSla;
    }

    @JsonProperty("made_sla")
    public void setMadeSla(String madeSla) {
        this.madeSla = madeSla;
    }

    @JsonProperty("caused_by")
    public String getCausedBy() {
        return causedBy;
    }

    @JsonProperty("caused_by")
    public void setCausedBy(String causedBy) {
        this.causedBy = causedBy;
    }

    @JsonProperty("watch_list")
    public String getWatchList() {
        return watchList;
    }

    @JsonProperty("watch_list")
    public void setWatchList(String watchList) {
        this.watchList = watchList;
    }

    @JsonProperty("upon_reject")
    public String getUponReject() {
        return uponReject;
    }

    @JsonProperty("upon_reject")
    public void setUponReject(String uponReject) {
        this.uponReject = uponReject;
    }

    @JsonProperty("sys_updated_on")
    public String getSysUpdatedOn() {
        return sysUpdatedOn;
    }

    @JsonProperty("sys_updated_on")
    public void setSysUpdatedOn(String sysUpdatedOn) {
        this.sysUpdatedOn = sysUpdatedOn;
    }

    @JsonProperty("child_incidents")
    public String getChildIncidents() {
        return childIncidents;
    }

    @JsonProperty("child_incidents")
    public void setChildIncidents(String childIncidents) {
        this.childIncidents = childIncidents;
    }

    @JsonProperty("hold_reason")
    public String getHoldReason() {
        return holdReason;
    }

    @JsonProperty("hold_reason")
    public void setHoldReason(String holdReason) {
        this.holdReason = holdReason;
    }

    @JsonProperty("approval_history")
    public String getApprovalHistory() {
        return approvalHistory;
    }

    @JsonProperty("approval_history")
    public void setApprovalHistory(String approvalHistory) {
        this.approvalHistory = approvalHistory;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("resolved_by")
    public LinkValue getResolvedBy() {
        return resolvedBy;
    }

    @JsonProperty("resolved_by")
    public void setResolvedBy(LinkValue resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @JsonProperty("sys_updated_by")
    public String getSysUpdatedBy() {
        return sysUpdatedBy;
    }

    @JsonProperty("sys_updated_by")
    public void setSysUpdatedBy(String sysUpdatedBy) {
        this.sysUpdatedBy = sysUpdatedBy;
    }

    @JsonProperty("opened_by")
    public LinkValue getOpenedBy() {
        return openedBy;
    }

    @JsonProperty("opened_by")
    public void setOpenedBy(LinkValue openedBy) {
        this.openedBy = openedBy;
    }

    @JsonProperty("user_input")
    public String getUserInput() {
        return userInput;
    }

    @JsonProperty("user_input")
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    @JsonProperty("sys_created_on")
    public String getSysCreatedOn() {
        return sysCreatedOn;
    }

    @JsonProperty("sys_created_on")
    public void setSysCreatedOn(String sysCreatedOn) {
        this.sysCreatedOn = sysCreatedOn;
    }

    @JsonProperty("sys_domain")
    public LinkValue getSysDomain() {
        return sysDomain;
    }

    @JsonProperty("sys_domain")
    public void setSysDomain(LinkValue sysDomain) {
        this.sysDomain = sysDomain;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("sys_created_by")
    public String getSysCreatedBy() {
        return sysCreatedBy;
    }

    @JsonProperty("sys_created_by")
    public void setSysCreatedBy(String sysCreatedBy) {
        this.sysCreatedBy = sysCreatedBy;
    }

    @JsonProperty("knowledge")
    public String getKnowledge() {
        return knowledge;
    }

    @JsonProperty("knowledge")
    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    @JsonProperty("order")
    public String getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(String order) {
        this.order = order;
    }

    @JsonProperty("calendar_stc")
    public String getCalendarStc() {
        return calendarStc;
    }

    @JsonProperty("calendar_stc")
    public void setCalendarStc(String calendarStc) {
        this.calendarStc = calendarStc;
    }

    @JsonProperty("closed_at")
    public String getClosedAt() {
        return closedAt;
    }

    @JsonProperty("closed_at")
    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    @JsonProperty("cmdb_ci")
    public LinkValue getCmdbCi() {
        return cmdbCi;
    }

    @JsonProperty("cmdb_ci")
    public void setCmdbCi(LinkValue cmdbCi) {
        this.cmdbCi = cmdbCi;
    }

    @JsonProperty("delivery_plan")
    public String getDeliveryPlan() {
        return deliveryPlan;
    }

    @JsonProperty("delivery_plan")
    public void setDeliveryPlan(String deliveryPlan) {
        this.deliveryPlan = deliveryPlan;
    }

    @JsonProperty("impact")
    public String getImpact() {
        return impact;
    }

    @JsonProperty("impact")
    public void setImpact(String impact) {
        this.impact = impact;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("work_notes_list")
    public String getWorkNotesList() {
        return workNotesList;
    }

    @JsonProperty("work_notes_list")
    public void setWorkNotesList(String workNotesList) {
        this.workNotesList = workNotesList;
    }

    @JsonProperty("business_service")
    public LinkValue getBusinessService() {
        return businessService;
    }

    @JsonProperty("business_service")
    public void setBusinessService(LinkValue businessService) {
        this.businessService = businessService;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("sys_domain_path")
    public String getSysDomainPath() {
        return sysDomainPath;
    }

    @JsonProperty("sys_domain_path")
    public void setSysDomainPath(String sysDomainPath) {
        this.sysDomainPath = sysDomainPath;
    }

    @JsonProperty("rfc")
    public String getRfc() {
        return rfc;
    }

    @JsonProperty("rfc")
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @JsonProperty("time_worked")
    public String getTimeWorked() {
        return timeWorked;
    }

    @JsonProperty("time_worked")
    public void setTimeWorked(String timeWorked) {
        this.timeWorked = timeWorked;
    }

    @JsonProperty("expected_start")
    public String getExpectedStart() {
        return expectedStart;
    }

    @JsonProperty("expected_start")
    public void setExpectedStart(String expectedStart) {
        this.expectedStart = expectedStart;
    }

    @JsonProperty("opened_at")
    public String getOpenedAt() {
        return openedAt;
    }

    @JsonProperty("opened_at")
    public void setOpenedAt(String openedAt) {
        this.openedAt = openedAt;
    }

    @JsonProperty("business_duration")
    public String getBusinessDuration() {
        return businessDuration;
    }

    @JsonProperty("business_duration")
    public void setBusinessDuration(String businessDuration) {
        this.businessDuration = businessDuration;
    }

    @JsonProperty("group_list")
    public String getGroupList() {
        return groupList;
    }

    @JsonProperty("group_list")
    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    @JsonProperty("work_end")
    public String getWorkEnd() {
        return workEnd;
    }

    @JsonProperty("work_end")
    public void setWorkEnd(String workEnd) {
        this.workEnd = workEnd;
    }

    @JsonProperty("caller_id")
    public LinkValue getCallerId() {
        return callerId;
    }

    @JsonProperty("caller_id")
    public void setCallerId(LinkValue callerId) {
        this.callerId = callerId;
    }

    @JsonProperty("reopened_time")
    public String getReopenedTime() {
        return reopenedTime;
    }

    @JsonProperty("reopened_time")
    public void setReopenedTime(String reopenedTime) {
        this.reopenedTime = reopenedTime;
    }

    @JsonProperty("resolved_at")
    public String getResolvedAt() {
        return resolvedAt;
    }

    @JsonProperty("resolved_at")
    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @JsonProperty("approval_set")
    public String getApprovalSet() {
        return approvalSet;
    }

    @JsonProperty("approval_set")
    public void setApprovalSet(String approvalSet) {
        this.approvalSet = approvalSet;
    }

    @JsonProperty("subcategory")
    public String getSubcategory() {
        return subcategory;
    }

    @JsonProperty("subcategory")
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @JsonProperty("work_notes")
    public String getWorkNotes() {
        return workNotes;
    }

    @JsonProperty("work_notes")
    public void setWorkNotes(String workNotes) {
        this.workNotes = workNotes;
    }

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("close_code")
    public String getCloseCode() {
        return closeCode;
    }

    @JsonProperty("close_code")
    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    @JsonProperty("correlation_display")
    public String getCorrelationDisplay() {
        return correlationDisplay;
    }

    @JsonProperty("correlation_display")
    public void setCorrelationDisplay(String correlationDisplay) {
        this.correlationDisplay = correlationDisplay;
    }

    @JsonProperty("delivery_task")
    public String getDeliveryTask() {
        return deliveryTask;
    }

    @JsonProperty("delivery_task")
    public void setDeliveryTask(String deliveryTask) {
        this.deliveryTask = deliveryTask;
    }

    @JsonProperty("work_start")
    public String getWorkStart() {
        return workStart;
    }

    @JsonProperty("work_start")
    public void setWorkStart(String workStart) {
        this.workStart = workStart;
    }

    @JsonProperty("assignment_group")
    public LinkValue getAssignmentGroup() {
        return assignmentGroup;
    }

    @JsonProperty("assignment_group")
    public void setAssignmentGroup(LinkValue assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    @JsonProperty("additional_assignee_list")
    public String getAdditionalAssigneeList() {
        return additionalAssigneeList;
    }

    @JsonProperty("additional_assignee_list")
    public void setAdditionalAssigneeList(String additionalAssigneeList) {
        this.additionalAssigneeList = additionalAssigneeList;
    }

    @JsonProperty("business_stc")
    public String getBusinessStc() {
        return businessStc;
    }

    @JsonProperty("business_stc")
    public void setBusinessStc(String businessStc) {
        this.businessStc = businessStc;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("calendar_duration")
    public String getCalendarDuration() {
        return calendarDuration;
    }

    @JsonProperty("calendar_duration")
    public void setCalendarDuration(String calendarDuration) {
        this.calendarDuration = calendarDuration;
    }

    @JsonProperty("close_notes")
    public String getCloseNotes() {
        return closeNotes;
    }

    @JsonProperty("close_notes")
    public void setCloseNotes(String closeNotes) {
        this.closeNotes = closeNotes;
    }

    @JsonProperty("notify")
    public String getNotify() {
        return notify;
    }

    @JsonProperty("notify")
    public void setNotify(String notify) {
        this.notify = notify;
    }

    @JsonProperty("sys_class_name")
    public String getSysClassName() {
        return sysClassName;
    }

    @JsonProperty("sys_class_name")
    public void setSysClassName(String sysClassName) {
        this.sysClassName = sysClassName;
    }

    @JsonProperty("closed_by")
    public LinkValue getClosedBy() {
        return closedBy;
    }

    @JsonProperty("closed_by")
    public void setClosedBy(LinkValue closedBy) {
        this.closedBy = closedBy;
    }

    @JsonProperty("follow_up")
    public String getFollowUp() {
        return followUp;
    }

    @JsonProperty("follow_up")
    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    @JsonProperty("parent_incident")
    public LinkValue getParentIncident() {
        return parentIncident;
    }

    @JsonProperty("parent_incident")
    public void setParentIncident(LinkValue parentIncident) {
        this.parentIncident = parentIncident;
    }

    @JsonProperty("sys_id")
    public String getSysId() {
        return sysId;
    }

    @JsonProperty("sys_id")
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @JsonProperty("contact_type")
    public String getContactType() {
        return contactType;
    }

    @JsonProperty("contact_type")
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    @JsonProperty("reopened_by")
    public String getReopenedBy() {
        return reopenedBy;
    }

    @JsonProperty("reopened_by")
    public void setReopenedBy(String reopenedBy) {
        this.reopenedBy = reopenedBy;
    }

    @JsonProperty("incident_state")
    public String getIncidentState() {
        return incidentState;
    }

    @JsonProperty("incident_state")
    public void setIncidentState(String incidentState) {
        this.incidentState = incidentState;
    }

    @JsonProperty("urgency")
    public String getUrgency() {
        return urgency;
    }

    @JsonProperty("urgency")
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @JsonProperty("problem_id")
    public LinkValue getProblemId() {
        return problemId;
    }

    @JsonProperty("problem_id")
    public void setProblemId(LinkValue problemId) {
        this.problemId = problemId;
    }

    @JsonProperty("company")
    public LinkValue getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(LinkValue company) {
        this.company = company;
    }

    @JsonProperty("reassignment_count")
    public String getReassignmentCount() {
        return reassignmentCount;
    }

    @JsonProperty("reassignment_count")
    public void setReassignmentCount(String reassignmentCount) {
        this.reassignmentCount = reassignmentCount;
    }

    @JsonProperty("activity_due")
    public String getActivityDue() {
        return activityDue;
    }

    @JsonProperty("activity_due")
    public void setActivityDue(String activityDue) {
        this.activityDue = activityDue;
    }

    @JsonProperty("assigned_to")
    public LinkValue getAssignedTo() {
        return assignedTo;
    }

    @JsonProperty("assigned_to")
    public void setAssignedTo(LinkValue assignedTo) {
        this.assignedTo = assignedTo;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JsonProperty("comments")
    public String getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(String comments) {
        this.comments = comments;
    }

    @JsonProperty("approval")
    public String getApproval() {
        return approval;
    }

    @JsonProperty("approval")
    public void setApproval(String approval) {
        this.approval = approval;
    }

    @JsonProperty("sla_due")
    public String getSlaDue() {
        return slaDue;
    }

    @JsonProperty("sla_due")
    public void setSlaDue(String slaDue) {
        this.slaDue = slaDue;
    }

    @JsonProperty("comments_and_work_notes")
    public String getCommentsAndWorkNotes() {
        return commentsAndWorkNotes;
    }

    @JsonProperty("comments_and_work_notes")
    public void setCommentsAndWorkNotes(String commentsAndWorkNotes) {
        this.commentsAndWorkNotes = commentsAndWorkNotes;
    }

    @JsonProperty("due_date")
    public String getDueDate() {
        return dueDate;
    }

    @JsonProperty("due_date")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("sys_mod_count")
    public String getSysModCount() {
        return sysModCount;
    }

    @JsonProperty("sys_mod_count")
    public void setSysModCount(String sysModCount) {
        this.sysModCount = sysModCount;
    }

    @JsonProperty("reopen_count")
    public String getReopenCount() {
        return reopenCount;
    }

    @JsonProperty("reopen_count")
    public void setReopenCount(String reopenCount) {
        this.reopenCount = reopenCount;
    }

    @JsonProperty("sys_tags")
    public String getSysTags() {
        return sysTags;
    }

    @JsonProperty("sys_tags")
    public void setSysTags(String sysTags) {
        this.sysTags = sysTags;
    }

    @JsonProperty("escalation")
    public String getEscalation() {
        return escalation;
    }

    @JsonProperty("escalation")
    public void setEscalation(String escalation) {
        this.escalation = escalation;
    }

    @JsonProperty("upon_approval")
    public String getUponApproval() {
        return uponApproval;
    }

    @JsonProperty("upon_approval")
    public void setUponApproval(String uponApproval) {
        this.uponApproval = uponApproval;
    }

    @JsonProperty("correlation_id")
    public String getCorrelationId() {
        return correlationId;
    }

    @JsonProperty("correlation_id")
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @JsonProperty("location")
    public LinkValue getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(LinkValue location) {
        this.location = location;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
