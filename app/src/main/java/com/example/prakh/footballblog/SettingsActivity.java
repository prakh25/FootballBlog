package com.example.prakh.footballblog;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.prakh.footballblog.search.RecentSuggestionsProvider;

/**
 * Created by prakh on 19-11-2017.
 */
// TODO: create new settings page and include settings to change theme accent colors
    // TODO: add clear search history settings options
public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    public static Intent createNewIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_title_ringtone)));

            findPreference(getString(R.string.pref_title_clear_search_history))
                    .setOnPreferenceClickListener(preference -> {

                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                                getActivity(), RecentSuggestionsProvider.AUTHORITY,
                                RecentSuggestionsProvider.MODE);

                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.pref_title_clear_search_history))
                                .setMessage(getString(R.string.pref_clear_search_history_dialog_message))
                                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                                    suggestions.clearHistory();
                                    dialogInterface.dismiss();
                                })
                                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                                    dialogInterface.cancel();
                                })
                                .show();

                        return true;

                    });
        }
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, newValue) -> {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else if (preference instanceof RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_ringtone_silent);

            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue));

                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.setSummary(R.string.summary_choose_ringtone);
                } else {
                    // Set the summary to reflect the new ringtone display
                    // name.
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }

        } else if (preference instanceof EditTextPreference) {
            if (preference.getKey().equals("key_gallery_name")) {
                // update the changed gallery name to summary filed
                preference.setSummary(stringValue);
            }
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    };
}
