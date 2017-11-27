package masegi.sho.mytimetable.domain.value;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public enum License {

    Apache("Apache.txt"),
    MIT("MIT.txt");

    private String filePath;

    License(String filePath) {
        this.filePath = filePath;
    }

    public String getContentString(Context context) {

        String text = "";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("licenses/" + filePath)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {

                text += mLine;
                text += '\n';
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }

        return text;
    }
}
