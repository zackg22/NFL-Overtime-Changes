import java.util.*;
import java.io.*;

public class AddWinDiff {

  Map<String, Integer> buildResultsMap(Scanner[] scanners) {
    Map<String, Integer> winsMap = new HashMap<>();

    try {
      int season = 2012;
      for (Scanner scanner : scanners) {
        processScanner(scanner, winsMap, season);
        season++;
      }
    } catch (Exception e) {
      System.out.println("There was an error on the play");
      System.out.println(e);
    }

    return winsMap;
  }

  void processScanner(Scanner reader, Map<String, Integer> winsMap, int season) throws FileNotFoundException{
    reader.nextLine();
    while (reader.hasNextLine()) {
      String line = reader.nextLine();
      String[] arr = line.split(",");

      String teamName = arr[0];
      String[] teamArr = teamName.split(" ");
      String teamNameNoLoc = teamArr[(teamArr.length - 1)];

      int wins = Integer.parseInt(arr[1]);
      String entry = season + teamNameNoLoc;

      winsMap.put(entry, wins);
    }
  }

  void addWinDiffCol(Scanner reader, BufferedWriter writer, Map<String, Integer> winsMap) throws IOException{
    reader.nextLine();
    while (reader.hasNextLine()) {
      String line = reader.nextLine();
      String[] arr = line.split(",");

      String team1 = arr[1];
      String team2 = arr[2];

      String date = arr[0];
      String[] splitDate = date.split("-");
      int season = Integer.parseInt(splitDate[0]);
      String month = splitDate[1];
      if (month.equals("01")) {
        season--;
      }

      String team1Entry = season + team1;
      String team2Entry = season + team2;

      int team1Wins = winsMap.get(team1Entry);
      int team2Wins = winsMap.get(team2Entry);

      int winDiff = team1Wins - team2Wins;
      writer.write(line + "," + winDiff + "\n");
    }
  }

  public static void main(String[] args)
    {
        AddWinDiff builder = new AddWinDiff();

        try {
          Scanner reader12 = new Scanner(new FileInputStream("Data/2012res.csv"));
          Scanner reader13 = new Scanner(new FileInputStream("Data/2013res.csv"));
          Scanner reader14 = new Scanner(new FileInputStream("Data/2014res.csv"));
          Scanner reader15 = new Scanner(new FileInputStream("Data/2015res.csv"));
          Scanner reader16 = new Scanner(new FileInputStream("Data/2016res.csv"));
          Scanner[] scanners = new Scanner[]{reader12, reader13, reader14, reader15, reader16};
          Map<String, Integer> winsMap = builder.buildResultsMap(scanners);

          for (String entry : winsMap.keySet()) {
            int wins = winsMap.get(entry);
            System.out.println(entry + ": " + wins);
          }

          Scanner readerWon = new Scanner(new FileInputStream("Data/kickoffWon.csv"));
          BufferedWriter writerWon = new BufferedWriter(new FileWriter("Data/wonAppended.csv"));
          builder.addWinDiffCol(readerWon, writerWon, winsMap);
          writerWon.close();

          Scanner readerLost = new Scanner(new FileInputStream("Data/kickoffLost.csv"));
          BufferedWriter writerLost = new BufferedWriter(new FileWriter("Data/lostAppended.csv"));
          builder.addWinDiffCol(readerLost, writerLost, winsMap);
          writerLost.close();

        } catch (IOException e) {
          System.out.println("There was an error reading or writing the files");
        }
    }
}
