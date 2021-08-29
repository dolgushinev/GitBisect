public class CheckResult {
    String commit;
    Boolean checkResult;

    public String getCommit() {
        return commit;
    }

    public Boolean getCheckResult() {
        return checkResult;
    }

    public CheckResult(String commit, Boolean checkResult) {
        this.commit = commit;
        this.checkResult = checkResult;
    }
}
