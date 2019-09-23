import java.util.Scanner;

public class Main3
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] counter = new int[n];
        int max = 0;

        for (int i = 0; i < n; i++)
        {
            int number = scanner.nextInt();
            counter[number]++;

            if (number > max)
                max = number;
        }

        int ans = -1;

        for (int i = 1; i <= max; i++)
        {
            if ((ans == -1) || (counter[ans] > counter[i]) || ((counter[ans] == counter[i]) && (i < ans)))
                ans = i;
        }

        System.out.println(ans);
    }
}