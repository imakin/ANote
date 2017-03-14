package in.izzulmak.anote.room.main;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Izzulmakin on 14/03/17.
 */
public class CustomButton extends Button {
    String urlaction;
    public CustomButton(Context context, final String urlaction) throws MalformedURLException {
        super(context);

        final URL url = new URL(urlaction);
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream inp = new BufferedInputStream(urlc.getInputStream());
                    } finally {
                        urlc.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
