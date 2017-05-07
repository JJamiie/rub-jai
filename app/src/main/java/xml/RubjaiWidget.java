package xml;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.views.activity.ExpenseIncomeActivity;
import com.rashata.jamie.spend.views.activity.MainActivity;

import java.util.Calendar;

import rx.functions.Action1;

/**
 * Implementation of App Widget functionality.
 */
public class RubjaiWidget extends AppWidgetProvider {
    private String summary;
    private int[] ids;


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //set Alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra("ids", ids);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar current = Calendar.getInstance();
        Calendar nexMin = (Calendar) current.clone();
        nexMin.set(Calendar.MINUTE, nexMin.get(Calendar.MINUTE) + 1);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // bannerAd alarm
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nexMin.getTimeInMillis() - current.getTimeInMillis(), 60000, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nexMin.getTimeInMillis() - current.getTimeInMillis(), pendingIntent);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rubjai_widget);
        views.setTextViewText(R.id.txt_summary_today, getSummaryToday());

        views.setTextViewText(R.id.txt_today, Contextor.getInstance().getContext().getString(R.string.today));


        Intent intent_open = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent_open_app = PendingIntent.getActivity(context, 0, intent_open, 0);
        views.setOnClickPendingIntent(R.id.btn_open_app, pendingIntent_open_app);

        Intent intent_expense = new Intent(context, ExpenseIncomeActivity.class);
        intent_expense.putExtra("type", Data.TYPE_EXPENSE);
        PendingIntent pendingIntent_expense = PendingIntent.getActivity(context, 1, intent_expense, 0);
        views.setOnClickPendingIntent(R.id.btn_expense, pendingIntent_expense);
        views.setTextViewText(R.id.btn_expense, Contextor.getInstance().getContext().getString(R.string.expense));

        Intent intent_income = new Intent(context, ExpenseIncomeActivity.class);
        intent_income.putExtra("type", Data.TYPE_INCOME);
        PendingIntent pendingIntent_income = PendingIntent.getActivity(context, 2, intent_income, 0);
        views.setOnClickPendingIntent(R.id.btn_income, pendingIntent_income);
        views.setTextViewText(R.id.btn_income, Contextor.getInstance().getContext().getString(R.string.income));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        ids = appWidgetIds;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public String getSummaryToday() {
        RealmManager.getInstance().getDataRepository().getSummaryToday().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                summary = s;
            }
        });
        return summary;
    }
}

