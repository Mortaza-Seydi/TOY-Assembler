import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static HashMap<String, Integer> labels = new HashMap<>();
    private static HashMap<String, Integer> variables = new HashMap<>();

    private static ArrayList<String> labelList = new ArrayList<>();
    private static ArrayList<String> variableList = new ArrayList<>();

    private static int currentMemAddress = 0;
    private static int currentInsAddress =0;

    public static void main(String[] args)
    {
        try
        {
            File in = new File("src/code.txt");
            File out = new File("src/ins.txt");

            FileInputStream fileInputStream = new FileInputStream(in);
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNext())
            {
                String ins = scanner.nextLine();

                if (!ins.isEmpty())
                {
                    String code = decode(ins);
                    if (code != null)
                        dataOutputStream.writeBytes(code + "\n");
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public static String decode(String ins)
    {
        StringBuilder builder = new StringBuilder();
        String opCode = ins.split(" ")[0];
        String address = null;

        if (!opCode.contains("TAT") && !opCode.contains("ROR"))
            address = ins.split(" ")[1];

        if (opCode.contains(":"))
        {
            String label = opCode.split(":")[0];
            labels.put(label, currentInsAddress);
            labelList.add(label);

            opCode = ins.split(" ")[1];

            if (!opCode.contains("TAT") && !opCode.contains("ROR"))
                address = ins.split(" ")[2];
        }

        else if (opCode.contains(".ORG"))
        {
            currentMemAddress = Integer.parseInt(ins.split(" ")[1]);
            //currentInsAddress++;
            return null;
        }

        else if (opCode.contains(".DATA"))
        {
            String var = ins.split(" ")[1];
            try
            {
                int value = Integer.parseInt(ins.split(" ")[2]);
                addValueToMem(currentMemAddress, value);
                variableList.add(var);
                variables.put(var, currentMemAddress);
                currentMemAddress++;
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                variableList.add(var);
                variables.put(var, currentMemAddress);
                currentMemAddress++;
            }
            //currentInsAddress++;
            return null;
        }

        switch (opCode)
        {
            case "JMP" :
                builder.append("0000");//0
                break;

            case "ADC" :
                builder.append("0001");//1
                break;

            case "XOR" :
                builder.append("0010");//2
                break;

            case "SBC" :
                builder.append("0011");//3
                break;

            case "ROR" :
                builder.append("0100");//4
                break;

            case "TAT" :
                builder.append("0101");//5
                break;

            case "OR" :
                builder.append("0110");//6
                break;

            case "AND" :
                builder.append("1000");//8
                break;

            case "LDC" :
                builder.append("1001");//9
                break;

            case "BCC" :
                builder.append("1010");//10
                break;

            case "BNE" :
                builder.append("1011");//11
                break;

            case "LDI" :
                builder.append("1100");//12
                break;

            case "STT" :
                builder.append("1101");//13
                break;

            case "LDA" :
                builder.append("1110");//14
                break;

            case "STA" :
                builder.append("1111");//15
                break;

            default:
                builder.append("0111");//7
                break;

        }

        if (!opCode.contains("TAT") && !opCode.contains("ROR"))
        {
            try
            {
                if (Integer.parseInt(address) < 4097)
                {
                    builder.append(tonBit(12, Integer.parseInt(address)));
                }
            }
            catch (NumberFormatException e)
            {
                if (labelList.contains(address))
                    builder.append(tonBit(12, labels.get(address)));

                else if (variableList.contains(address))
                    builder.append(tonBit(12, variables.get(address)));
            }
        }

        currentInsAddress++;
        return builder.toString();
    }

    private static String tonBit(int n, int integer)
    {
        String temp = Integer.toBinaryString(integer);
        StringBuilder tempBuilder = new StringBuilder();

        for (int i = 0; i < n - temp.length(); i++)
        {
            tempBuilder.append("0");
        }
        tempBuilder.append(temp);

        return tempBuilder.toString();
    }

    private static void addValueToMem(int address, Integer value)
    {
        String val = tonBit(16, value);

        try
        {
            File file = new File("src/mem.txt");
            List<String> lines = Files.readAllLines(file.toPath());
            lines.set(address+3, val);
            Files.write(file.toPath(), lines);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}