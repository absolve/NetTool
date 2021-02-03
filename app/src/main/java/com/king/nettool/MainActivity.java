package com.king.nettool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;

import org.slf4j.Logger;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final Console con = System.console();

    @BindView(R.id.btn_test)
    public Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_test)
    public void onClick(View view) {
        int id = view.getId();
        Log.d("---","onClick");
        if(id==R.id.btn_test){
            Log.d("---","onClick");
            final SSHClient ssh = new SSHClient();

            Session session = null;
            try {
//                ssh.loadKnownHosts();
                ssh.connect("localhost");
//                ssh.authPublickey(System.getProperty("user.name"));
                session = ssh.startSession();
                final Session.Command cmd = session.exec("ping -c 1 google.com");
                con.writer().print(IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(5, TimeUnit.SECONDS);
                con.writer().print("\n** exit status: " + cmd.getExitStatus());
            }  catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (session != null) {
                        session.close();
                    }
                    ssh.disconnect();
                } catch (IOException e) {
                    // Do Nothing
                    e.printStackTrace();
                }
            }

        }
    }
}