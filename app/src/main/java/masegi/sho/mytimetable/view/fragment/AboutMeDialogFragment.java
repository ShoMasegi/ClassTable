package masegi.sho.mytimetable.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.vansuita.materialabout.builder.AboutBuilder;

import masegi.sho.mytimetable.R;

/**
 * Created by masegi on 2017/11/23.
 */

public class AboutMeDialogFragment extends DialogFragment {

    public AboutMeDialogFragment() { }

    public static AboutMeDialogFragment newInstance() {

        return new AboutMeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Context context = getContext();
        AboutBuilder aboutBuilder = AboutBuilder.with(context)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setPhoto(R.drawable.profile)
                .setCover(R.drawable.wallpaper)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName("Sho Masegi")
                .setSubTitle("Mobile Developer")
                .setLinksColumnsCount(4)
                .setBrief(R.string.about_me)
                .addGooglePlayStoreLink("6057746053562508109")
                .addGitHubLink("ShoMasegi")
                .addBitbucketLink("m2_b0c27")
                .addFacebookLink("sho.masegi")
                .addTwitterLink("dev_boc")
                .addInstagramLink("boc_sho")
                .addEmailLink("m2dev.b0c27@gmail.com")
                .addWebsiteLink("https://qiita.com/ShoMasegi")
                .setVersionNameAsAppSubTitle()
                .setWrapScrollView(true)
                .setShowAsCard(true);
        return aboutBuilder.build();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();
        WindowManager.LayoutParams layoutParams =
                dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeidht = (int) (metrics.heightPixels * 0.8);
        layoutParams.width = dialogWidth;
        layoutParams.height = dialogHeidht;
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
