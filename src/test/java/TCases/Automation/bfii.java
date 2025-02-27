package TCases.Automation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.*;
import org.testng.annotations.Test;

public class bfii {

    private static final String HOST = "pp2.humanbrain.in";
    private static final int PORT = 22;
    private static final String USER = "appUser";
    private static final String PASSWORD = "Brain@123";

    @Test
    public void testMissingFiles() {
        // Prompt the user for manual input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Biosample: ");
        String biosample = scanner.nextLine().trim();
        System.out.print("Enter Series (e.g., BFI, MRI): ");
        String series = scanner.nextLine().trim();

        // Retrieve missing sections based on the remote file listing
        List<Integer> missingSections = findMissingSections(biosample, series);
        System.out.println("Missing Sections (Test): " + missingSections);
        scanner.close();
    }

    public static List<Integer> findMissingSections(String biosample, String series) {
        List<Integer> missingSections = new ArrayList<>();
        Set<Integer> existingSections = new HashSet<>();

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST, PORT);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String path = "/lustre/data/store10PB/repos1/iitlab/humanbrain/analytics/" + biosample + "/" + series;
            // Command to list files matching the _thumbnail_small.jpg pattern
            String listCommand = "cd " + path + " && ls -alh | grep '_thumbnail_small.jpg'";
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(listCommand);
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err);

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Debug output to verify the file listing
                System.out.println("DEBUG: " + line);
                String sectionNumber = extractSectionNumber(line);
                if (sectionNumber != null) {
                    try {
                        existingSections.add(Integer.parseInt(sectionNumber));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing section number: " + sectionNumber);
                    }
                }
            }
            reader.close();
            channelExec.disconnect();
            session.disconnect();

            // Determine missing sections based on the contiguous range found
            if (!existingSections.isEmpty()) {
                int minSection = Collections.min(existingSections);
                int maxSection = Collections.max(existingSections);
                for (int i = minSection; i <= maxSection; i++) {
                    if (!existingSections.contains(i)) {
                        missingSections.add(i);
                    }
                }
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
            System.out.println("❌ SSH Connection Failed! Check Network & Credentials.");
        }
        return missingSections;
    }

    /**
     * Extracts the section number from a filename line using a regex.
     * Expected file pattern (any leading info allowed):
     * ...BFI-SE_<number>_thumbnail_small.jpg...
     */
    public static String extractSectionNumber(String line) {
        Pattern pattern = Pattern.compile(".*BFI-SE_(\\d+)_thumbnail_small\\.jpg.*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
