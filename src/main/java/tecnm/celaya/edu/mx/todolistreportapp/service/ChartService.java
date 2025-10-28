package tecnm.celaya.edu.mx.todolistreportapp.service;

import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDaoImpl;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;

public class ChartService {

    private final TareaDao tareaDao;

    public ChartService() {
        this.tareaDao = new TareaDaoImpl();
    }

    public Map<String, Integer> getDetailedStatusDataForPeriod(int userId, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateFromPeriod(period, endDate);
        Map<String, Integer> dataFromDao = tareaDao.getTaskCountsByStatus(userId, startDate, endDate);
        System.out.println("[DIAGNÓSTICO 2 - Service] Mapa de estados recibido del DAO: " + dataFromDao.toString());
        return dataFromDao;
    }

    public Map<String, Integer> getCategoryDataForPeriod(int userId, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateFromPeriod(period, endDate);
        Map<String, Integer> dataFromDao = tareaDao.getTaskCategoryCounts(userId, startDate, endDate);
        System.out.println("[DIAGNÓSTICO 2 - Service] Mapa de categorías recibido del DAO: " + dataFromDao.toString());
        return dataFromDao;
    }

    private LocalDate getStartDateFromPeriod(String period, LocalDate endDate) {
        return switch (period) {
            case "Semana" -> endDate.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
            case "Mes" -> endDate.withDayOfMonth(1);
            case "Año" -> endDate.withDayOfYear(1);
            default -> LocalDate.of(1970, 1, 1);
        };
    }
}
