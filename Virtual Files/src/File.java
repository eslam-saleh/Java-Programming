import java.util.*;

class File {
    String filePath, fileName;
    private ArrayList<Integer> allocatedBlocks = new ArrayList<>();
    boolean deleted = false;

    File() {
        ;
    }

    File(String fPath) {
        if (fPath.charAt(fPath.length() - 1) == '\r' || fPath.charAt(fPath.length() - 1) == '\n')
            this.filePath = fPath.substring(0, fPath.length() - 1);
        else this.filePath = fPath;

        for (int i = filePath.length() - 1; i > 0; i--) {
            if (filePath.charAt(i) == '/') {
                fileName = filePath.substring(i + 1);
                break;
            }
        }
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }
}