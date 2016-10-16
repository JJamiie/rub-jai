package xml;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.component.Injector;
import com.rashata.jamie.spend.repository.DataRepository;
import com.rashata.jamie.spend.views.activity.ExpenseActivity;
import com.rashata.jamie.spend.views.activity.IncomeActivity;
import com.rashata.jamie.spend.views.activity.MainActivity;

import javax.inject.Inject;

import butterknife.Bind;
import rx.functions.Action1;

/**
 * Implementation of App Widget functionality.
 */
public class RubjaiWidget extends AppWidgetProvider {
    @Inject
    DataRepository dataRepository;
    private String summary;


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rubjai_widget);
        views.setTextViewText(R.id.txt_summary_today, getSummaryToday());

        Intent intent_open = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent_open_app = PendingIntent.getActivity(context, 0, intent_open, 0);
        views.setOnClickPendingIntent(R.id.btn_open_app, pendingIntent_open_app);

        Intent intent_expense = new Intent(context, ExpenseActivity.class);
        PendingIntent pendingIntent_expense = PendingIntent.getActivity(context, 0, intent_expense, 0);
        views.setOnClickPendingIntent(R.id.btn_expense, pendingIntent_expense);

        Intent intent_income = new Intent(context, IncomeActivity.class);
        PendingIntent pendingIntent_income = PendingIntent.getActivity(context, 0, intent_income, 0);
        views.setOnClickPendingIntent(R.id.btn_income, pendingIntent_income);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Injector.getApplicationComponent().inject(this);
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
        dataRepository.getSummaryToday().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                summary = s;
            }
        });
        return summary;
    }
}
