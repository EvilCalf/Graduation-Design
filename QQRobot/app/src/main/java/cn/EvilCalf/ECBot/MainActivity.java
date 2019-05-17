package cn.EvilCalf.ECBot;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private ListPreference mRobotListPreference;
    private EditTextPreference mApiKeyPreference;
    private Preference mAuthorPreference;
    private ListPreference mDonatePreference;
    private Preference mVersionPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mRobotListPreference = (ListPreference) findPreference("robot");
        mApiKeyPreference = (EditTextPreference) findPreference("robot_api_key");
        mAuthorPreference = findPreference("about_the_author");
        mDonatePreference = (ListPreference) findPreference("donate");
        mVersionPreference = findPreference("version");

        mRobotListPreference.setOnPreferenceChangeListener(this);
        mApiKeyPreference.setOnPreferenceChangeListener(this);
        mAuthorPreference.setOnPreferenceClickListener(this);
        mDonatePreference.setOnPreferenceChangeListener(this);
        mVersionPreference.setOnPreferenceClickListener(this);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            mVersionPreference.setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mApiKeyPreference.setSummary(mApiKeyPreference.getText());
        mRobotListPreference.setSummary(mRobotListPreference.getEntry());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File localFile = new File(Environment.getDataDirectory(), "data/cn.EvilCalf.ECBot/shared_prefs/cn.EvilCalf.qqrobot_preferences.xml");
        if (!localFile.exists()) {
            return;
        }
        localFile.setReadable(true, false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mRobotListPreference) {
            mRobotListPreference.setSummary(newValue.toString());
        }
        if (preference == mAuthorPreference) {
            mApiKeyPreference.setSummary(newValue.toString());
        }
        if (preference == mDonatePreference) {
            String s = newValue.toString();
            if (s.equals("支付宝")) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=HTTPS://QR.ALIPAY.COM/FKX02937IZP2U6EWMTOF9A"));
                try{
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "请先安装支付宝", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }
}
