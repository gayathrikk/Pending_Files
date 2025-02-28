package TCases.Automation;

import com.jcraft.jsch.*;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScaningPendingFIles {
    private static final int PORT = 22;
    private static final String USER = "appUser";
    private static final String PASSWORD = "Brain@123";

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

    private Map<String, List<String>> getFiles(String host, String directory) {
        List<String> pendingFiles = new ArrayList<>();
        List<String> currentFiles = new ArrayList<>();
        Map<String, List<String>> result = new HashMap<>();

        try {
            JSch jsch = new JSch();
            com.jcraft.jsch.Session sshSession = jsch.getSession(USER, host, PORT);  // Fully qualified for SSH Session
            sshSession.setPassword(PASSWORD);
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();

            ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
            channelExec.setCommand("ls -lh --time-style='+%b %d %H:%M' " + directory);
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err);

            BufferedReader reader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            channelExec.connect();

            String currentDate = getCurrentDate();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches(".*[0-9]{2}:[0-9]{2}.*")) { // Ensuring it matches timestamp format
                    if (line.contains(currentDate)) {
                        currentFiles.add(line);
                    } else {
                        pendingFiles.add(line);
                    }
                }
            }

            channelExec.disconnect();
            sshSession.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("currentFiles", currentFiles);
        result.put("pendingFiles", pendingFiles);
        return result;
    }

    @Test
    public void testFiles() {
        StringBuilder emailBody = new StringBuilder();
        boolean hasPendingFiles = false;

        for (Map.Entry<String, String> entry : MACHINES.entrySet()) {
            String host = entry.getKey();
            String directory = entry.getValue();

            Map<String, List<String>> files = getFiles(host, directory);
            List<String> currentFiles = files.get("currentFiles");
            List<String> pendingFiles = files.get("pendingFiles");

            System.out.println("\n================================== " + host + " ==================================");

            if (!currentFiles.isEmpty()) {
                System.out.println("Current Files:");
                System.out.println("---------------------------------------------------------------------------------------");
                System.out.println("Permissions    Size     Date       Time              Filename");
                System.out.println("---------------------------------------------------------------------------------------");
                for (String file : currentFiles) {
                    printFormattedFile(file);
                }
            } else {
                System.out.println("No new files added today on " + host);
            }

            System.out.println();

            if (!pendingFiles.isEmpty()) {
                hasPendingFiles = true;
                emailBody.append("\n========== ").append(host).append(" ==========\n");
                emailBody.append("Pending Files:\n");
                emailBody.append("Permissions      Size     Date       Time   Filename\n");
                emailBody.append("------------------------------------------------------\n");

                System.out.println("Pending Files:");
                System.out.println("Permissions      Size     Date       Time   Filename");
                System.out.println("------------------------------------------------------");
                for (String file : pendingFiles) {
                    printFormattedFile(file);
                    emailBody.append(file).append("\n");
                }
            } else {
                System.out.println("No pending files remaining on " + host);
            }

            Assert.assertNotNull(files, "Failed to fetch files from " + host);
        }

        // Send email if pending files exist
        if (hasPendingFiles) {
            sendEmail(emailBody.toString());
        }
    }

    private int getTotalPendingFiles() {
        // Logic to count the total number of pending files across all machines
        int total = 0;
        for (Map.Entry<String, String> entry : MACHINES.entrySet()) {
            String host = entry.getKey();
            Map<String, List<String>> files = getFiles(host, entry.getValue());
            List<String> pendingFiles = files.get("pendingFiles");
            total += pendingFiles.size();
        }
        return total;
    }

    private void printFormattedFile(String file) {
        String[] parts = file.split("\\s+");
        if (parts.length >= 9) {
            String permissions = parts[0];
            String size = parts[4];
            String date = parts[5] + " " + parts[6];
            String time = parts[7];
            String filename = String.join(" ", Arrays.copyOfRange(parts, 8, parts.length));

            System.out.printf("%-14s %-8s %-10s %-6s %s%n", permissions, size, date, time, filename);
        }
    }

    private void sendEmail(String emailBody) {
        final String senderEmail = "softwaretestingteam9@gmail.com"; // Change this
        final String senderPassword = "Health#123"; // Change this (Use App Password)
        final String recipientEmail = "annotatin.divya@gmail.com, venip@htic.iitm.ac.in, nathan.i@htic.iitm.ac.in"; // Change this

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        javax.mail.Session mailSession = javax.mail.Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Alert: Pending Files Found");
            message.setText("The following pending files were found:\n" + emailBody);

            // Logging the sending attempt
            System.out.println("Attempting to send email...");
            
            Transport.send(message);

            // Logging success
            System.out.println("Pending files email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
