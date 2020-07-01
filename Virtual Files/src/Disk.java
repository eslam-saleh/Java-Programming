import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

class Disk {
    private static int[] nBlocks = new int[100];
    private final static int blockSize = 1;
    private static int start = 0, length = 0, end = 0, index = 0, tmp = 0;
    private static ArrayList<Integer> list = new ArrayList<>();

    Disk() {
        for (int i : nBlocks) i = 0;
    }

    public static void loadBlocks() throws IOException {
        String txt = new String(Files.readAllBytes(Paths.get("Blocks.txt")));
        if (txt.length() > 0)
            for (int i = 0; i < nBlocks.length; i++)
                nBlocks[i] = (int) txt.charAt(i) - '0';
        else
            for (int i = 0; i < nBlocks.length; i++)
                Files.write(Paths.get("Blocks.txt"), (0 + "").getBytes(), StandardOpenOption.APPEND);
    }

    public static int[] getBlocks() {
        return nBlocks;
    }

    public static void setBlocks(ArrayList<Integer> allocatedBlocks) throws IOException {
        for (int i = 0; i < allocatedBlocks.size(); i++)
            nBlocks[allocatedBlocks.get(i)] = 1;

        new FileOutputStream("Blocks.txt");
        for (int i = 0; i < nBlocks.length; i++)
            Files.write(Paths.get("Blocks.txt"), (nBlocks[i] + "").getBytes(), StandardOpenOption.APPEND);

    }

    public static int getBlockSize() {
        return blockSize;
    }

    public static ArrayList<Integer> contiguous_allocation() throws IOException {
        System.out.print("\nEnter Start ,Length :: ");
        Scanner input = new Scanner(System.in);
        start = input.nextInt();
        length = input.nextInt();
        for (int i = start; i < start + length; i++)
            if (nBlocks[i] != 0) {
                System.out.println("Blocks are Already Used");
                return null;
            }
        for (int i = start; i < start + length; i++) {
            nBlocks[i] = 1;
            list.add(i);
        }
        System.out.println("Blocks :: ");
        for (int i = 0; i < nBlocks.length; i++) {
            System.out.print(nBlocks[i] + "\t");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println();
        FileOutputStream writer = new FileOutputStream("Blocks.txt");
        for (int i = 0; i < nBlocks.length; i++) {
            Files.write(Paths.get("Blocks.txt"), (nBlocks[i] + "").getBytes(), StandardOpenOption.APPEND);
        }
        return list;
    }

    public static ArrayList<Integer> linked_allocation() throws IOException {
        System.out.print("\nEnter Start ,End :: ");
        Scanner input = new Scanner(System.in);
        start = input.nextInt();
        end = input.nextInt();
        tmp = input.nextInt();

        while (tmp != end) {
            list.add(tmp);
            tmp = input.nextInt();
        }
        list.add(tmp);
        input.next();
        for (int i = 0; i < list.size(); i++)
            if (nBlocks[list.get(i)] != 0) {
                System.out.println("Blocks are Already Used");
                return null;
            }
        list = (ArrayList<Integer>) list.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < list.size(); i++) {
            nBlocks[list.get(i)] = 1;
        }

        System.out.println("Blocks :: ");
        for (int i = 0; i < nBlocks.length; i++) {
            System.out.print(nBlocks[i] + "\t");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println();
        FileOutputStream writer = new FileOutputStream("Blocks.txt");
        for (int i = 0; i < nBlocks.length; i++) {
            Files.write(Paths.get("Blocks.txt"), (nBlocks[i] + "").getBytes(), StandardOpenOption.APPEND);
        }
        return list;
    }

    public static ArrayList<Integer> indexed_allocation() throws IOException {
        System.out.print("\nEnter Index Block :: ");
        Scanner input = new Scanner(System.in);
        index = input.nextInt();
        input.nextInt();
        tmp = index;
        while (tmp != -1) {
            list.add(tmp);
            tmp = input.nextInt();
        }
        for (int i = 0; i < list.size(); i++)
            if (nBlocks[list.get(i)] != 0) {
                System.out.println("Blocks is Already Used");
                return null;
            }
        for (int i = 0; i < list.size(); i++) {
            nBlocks[list.get(i)] = 1;
        }

        System.out.println("Blocks :: ");
        for (int i = 0; i < nBlocks.length; i++) {
            System.out.print(nBlocks[i] + "\t");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println();
        FileOutputStream writer = new FileOutputStream("Blocks.txt");
        for (int i = 0; i < nBlocks.length; i++) {
            Files.write(Paths.get("Blocks.txt"), (nBlocks[i] + "").getBytes(), StandardOpenOption.APPEND);
        }
        return list;
    }

    public static void deleteBlocks(ArrayList<Integer> allocatedBlocks, String fName) throws IOException {
        for (int i = 0; i < allocatedBlocks.size(); i++) {
            nBlocks[allocatedBlocks.get(i)] = 0;
        }
        FileOutputStream writer = new FileOutputStream("Blocks.txt");
        for (int i = 0; i < nBlocks.length; i++)
            Files.write(Paths.get("Blocks.txt"), (nBlocks[i] + "").getBytes(), StandardOpenOption.APPEND);


        String txt = new String(Files.readAllBytes(Paths.get("Files.txt")));
        String[] parts = txt.split("\n");
        FileOutputStream writer2 = new FileOutputStream("Files.txt");
        if (txt.length() > 0)
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].charAt(parts[i].length() - 1) == '\r' || parts[i].charAt(parts[i].length() - 1) == '\n')
                    parts[i] = parts[i].substring(0, parts[i].length() - 1);
                if (!parts[i].equals(fName))
                    Files.write(Paths.get("Files.txt"), (parts[i] + '\n').getBytes(), StandardOpenOption.APPEND);
                else i++;
            }
    }
}
