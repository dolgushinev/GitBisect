import java.io.*;
import java.util.ArrayList;

public class GitBisect {
    public static void main(String[] args) throws IOException, InterruptedException {

        if (validateParam(args)) {
            System.out.println("result: " + getCommit(args[0], args[1], args[2]));
        } else {
            System.out.println("Пример корректного запуска утилиты:");
            System.out.println("java -jar target/git-bisect-1.0.jar 7f777ed95a19224294949e1b4ce56bbffcb1fe9f dd104400dc551dd4098f35e12072e12c45822adc testBisect\n");
        }
    }

    public static boolean validateParam(String[] args) {

        boolean isValid = true;

        if (args.length != 3) {
            System.out.println("Ошибка валидации параметров");
            isValid = false;
        } else if (!args[2].equals("testBisect")) {
            System.out.println("Указана несуществующая команда");
            isValid = false;
        }

        return isValid;
    }

    public static String getCommit(String lastCommitInOldState, String firstCommitInNewState, String command) throws IOException, InterruptedException {

        if (command.equals("testBisect")) {

            ArrayList<String> commits = getAndSaveCommits(lastCommitInOldState, firstCommitInNewState);

            if (commits.size() == 1) return "No result";

            String[] checkCommand = {""};

            ArrayList<CheckResult> checkResults = new ArrayList<>();

            for (int i = commits.size() - 1; i >= 0; i--) {
                checkCommand = new String[]{"sh", "./src/main/resources/checkBisect.sh", commits.get(i)};
                Process p = Runtime.getRuntime().exec(checkCommand);
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    checkResults.add(new CheckResult(commits.get(i), Boolean.parseBoolean(reader.readLine())));
                }
                p.waitFor();
            }

            return search(0, checkResults.size() - 1, checkResults);
        }
        return "No result";
    }

    private static ArrayList<String> getAndSaveCommits(String lastCommitInOldState, String firstCommitInNewState) throws IOException, InterruptedException {

        String getCommitsParam = new String(lastCommitInOldState + ".." + firstCommitInNewState);

        String[] command = {"sh", "./src/main/resources/getCommits.sh", getCommitsParam};
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();

        File file = new File("./commits.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;

        ArrayList<String> commits = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            commits.add(line);
        }

        commits.add(lastCommitInOldState);

        return commits;

    }

    static String search(int a, int b, ArrayList<CheckResult> checkResults) {

        int l = a;
        int r = b;

        while (l < r) {
            int mid = (l + r) / 2;
            if (function(mid, checkResults) == true) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return checkResults.get(r).getCommit();
    }

    static boolean function(int i, ArrayList<CheckResult> checkResults) {
        return checkResults.get(i).getCheckResult();
    }

}
