package TCases.Automation;
import com.jcraft.jsch.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Timer;
import java.util.TimerTask;

public class PendingFilesTest {
	 private static final int PORT = 22;
	    private static final String USER = "appUser";
	    private static final String PASSWORD = "Brain@123";
	    private static final String EMAIL_RECIPIENT = "annotation.divya@gmail.com";
	    private static final String EMAIL_SENDER = "softwaretestingteam9@gmail.com";
	    private static final String SMTP_HOST = "smtp.example.com";
	    private static final String SMTP_USER = "smtp_user";
	    private static final String SMTP_PASSWORD = "smtp_password";
	    
	    private static final Map<String, String> MACHINES = new HashMap<>();
	    static {
	        MACHINES.put("pp1.humanbrain.in", "/store/nvmestorage/postImageProcessor");
	        MACHINES.put("pp2.humanbrain.in", "/mnt/local/nvmestorage/postImageProcessor");
	        MACHINES.put("pp3.humanbrain.in", "/mnt/local/nvmestorage/postImageProcessor");
	        MACHINES.put("pp4.humanbrain.in", "/mnt/local/nvmestorage/postImageProcessor");
	        MACHINES.put("pp5.humanbrain.in", "/mnt/local/nvmestorage/postImageProcessor");
	        MACHINES.put("pp7.humanbrain.in", "/mnt/local/nvmestorage/postImageProcessor");
	        MACHINES.put("qd4.humanbrain.in", "/mnt/local/nvme2/postImageProcessor");
	    }

	    private String getCurrentDate() {
	        return new SimpleDateFormat("MMM dd").format(new Date()); // Example: Feb 27
	    }

	    private List<String> getPendingFiles(String host, String directory) {
	        List<String> pendingFiles = new ArrayList<>();
	        try {
	            JSch jsch = new JSch();
	            Session session = jsch.getSession(USER, host, PORT);
	            session.setPassword(PASSWORD);
	            session.setConfig("StrictHostKeyChecking", "no");
	            session.connect();

	            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
	            channelExec.setCommand("ls -lh --time-style='+%b %d %H:%M' " + directory);
	            channelExec.setInputStream(null);
	            channelExec.setErrStream(System.err);

	            BufferedReader reader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
	            channelExec.connect();

	            String currentDate = getCurrentDate();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (!line.contains(currentDate) && line.matches(".*[0-9]{2}:[0-9]{2}.*")) { // Ensuring it matches timestamp format
	                    pendingFiles.add(line);
	                }
	            }

	            channelExec.disconnect();
	            session.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return pendingFiles;
	    }

	    private void sendEmail(String subject, String body) {
	        Properties props = new Properties();
	        props.put("mail.smtp.host", SMTP_HOST);
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");

	        Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
	            }
	        });
	        
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(EMAIL_SENDER));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_RECIPIENT));
	            message.setSubject(subject);
	            message.setText(body);
	            Transport.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testPendingFiles() {
	        StringBuilder emailBody = new StringBuilder();
	        boolean hasPendingFiles = false;
	        
	        for (Map.Entry<String, String> entry : MACHINES.entrySet()) {
	            String host = entry.getKey();
	            String directory = entry.getValue();
	            List<String> pendingFiles = getPendingFiles(host, directory);
	            if (pendingFiles.isEmpty()) {
	                System.out.println("There are no pending files remaining on " + host);
	            } else {
	                hasPendingFiles = true;
	                emailBody.append("Pending files on ").append(host).append(":\n").append(String.join("\n", pendingFiles)).append("\n\n");
	            }
	            Assert.assertNotNull(pendingFiles, "Failed to fetch files from " + host);
	        }
	        
	        if (hasPendingFiles) {
	            sendEmail("Pending Files Alert", emailBody.toString());
	        }
	    }
	    
	    public static void main(String[] args) {
	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                new PendingFilesTest().testPendingFiles();
	            }
	        }, getInitialDelay(), 24 * 60 * 60 * 1000); // Run every 24 hours
	    }
	    
	    private static long getInitialDelay() {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, 8);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);

	        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
	        return delay > 0 ? delay : delay + 24 * 60 * 60 * 1000;
	    }
	}
