import java.util.Scanner;

public class Main2
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] counter = new int[101];
        int max = 0;

        for (int i = 0; i < n; i++)
        {
            int number = scanner.nextInt();
            counter[number]++;
            if (number > max)
                max = number;
        }

        int min = 100;
        int ans = 0;

        for (int i = 1; i <= max; i++)
        {
            if (counter[i] != 0)
            {
                if (counter[i] < min)
                {
                    min = counter[i];
                    ans = i;
                }
            }

        }

        System.out.println(ans);
    }
}