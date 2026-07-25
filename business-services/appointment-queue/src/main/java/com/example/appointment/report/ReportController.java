package com.example.appointment.report;
import com.example.appointment.appointment.*;
import java.time.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {
  private final AppointmentRepository repo;
  public ReportController(AppointmentRepository r) {
    repo = r;
  }
  @GetMapping("/daily")
  Report daily(@RequestParam LocalDate date) {
    return build(repo.findByAppointmentDateBetween(date, date));
  }
  @GetMapping("/monthly")
  Report monthly(@RequestParam int year, @RequestParam int month) {
    YearMonth y = YearMonth.of(year, month);
    return build(repo.findByAppointmentDateBetween(y.atDay(1), y.atEndOfMonth()));
  }
  private Report build(List<Appointment> a) {
    Map<String, Long> statuses = new TreeMap<>(), services = new TreeMap<>();
    long wait = 0, service = 0, completed = 0;
    for (Appointment x : a) {
      statuses.put(x.getStatus().name(), statuses.getOrDefault(x.getStatus().name(), 0L) + 1);
      services.put(
          x.getService().getName(), services.getOrDefault(x.getService().getName(), 0L) + 1);
      if (x.getCheckedInAt() != null && x.getStartedAt() != null)
        wait += Duration.between(x.getCheckedInAt(), x.getStartedAt()).toMinutes();
      if (x.getStartedAt() != null && x.getCompletedAt() != null) {
        service += Duration.between(x.getStartedAt(), x.getCompletedAt()).toMinutes();
        completed++;
      }
    }
    long no = statuses.getOrDefault("NO_SHOW", 0L);
    String popular = services.entrySet()
                         .stream()
                         .max(Map.Entry.comparingByValue())
                         .map(Map.Entry::getKey)
                         .orElse(null);
    return new Report(a.size(), statuses.getOrDefault("COMPLETED", 0L),
        statuses.getOrDefault("CANCELLED", 0L), no, a.isEmpty() ? 0 : no * 100.0 / a.size(),
        completed == 0 ? 0 : wait / (double) completed,
        completed == 0 ? 0 : service / (double) completed, popular, services, statuses, completed);
  }
  public static class Report {
    public final long totalAppointments, completedAppointments, cancelledAppointments, noShowCount,
        queueThroughput;
    public final double noShowPercentage, averageWaitingTimeMinutes, averageServiceTimeMinutes;
    public final String mostRequestedService;
    public final Map<String, Long> appointmentsByService, appointmentsByStatus;
    Report(long t, long c, long ca, long n, double np, double w, double s, String m,
        Map<String, Long> bs, Map<String, Long> st, long q) {
      totalAppointments = t;
      completedAppointments = c;
      cancelledAppointments = ca;
      noShowCount = n;
      noShowPercentage = np;
      averageWaitingTimeMinutes = w;
      averageServiceTimeMinutes = s;
      mostRequestedService = m;
      appointmentsByService = bs;
      appointmentsByStatus = st;
      queueThroughput = q;
    }
  }
}
