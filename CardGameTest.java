//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;

// 카드 클래스 정의
class Card {
    String pattern; // 카드 무늬 (하트, 다이아몬드, 스페이드, 클럽)
    String rank; // 카드 랭크 (A, 2, 3, ..., K)
    int value;   // 카드 값 (랭크에 따라 점수)

    // 생성자
    public Card(String pattern, String rank, int value) {
        this.pattern = pattern;
        this.rank = rank;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

// 플레이어 클래스 정의
class Player {
    private final String nickname;
    private int totalScore;

    public Player(String nickname) {
        this.nickname = nickname;
        this.totalScore = 0;
    }

    public void addScore(int score) {
        this.totalScore += score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getNickname() {
        return nickname;
    }
}

// 딜러 클래스
class Dealer1 {
    private final List<Card> deck;

    public Dealer1() {
        this.deck = initializeDeck();
    }

    private List<Card> initializeDeck() {
        List<Card> deck = new ArrayList<>();
        String[] patterns = {"Hearts", "Diamonds", "Spades", "Clubs"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        for (String pattern : patterns) {
            for (int i = 0; i < ranks.length; i++) {
                deck.add(new Card(pattern, ranks[i], values[i]));
            }
        }
        return deck;
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public List<Card> dealCards(int numberOfCards) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < numberOfCards && !deck.isEmpty(); i++) {
            hand.add(deck.remove(0));
        }
        return hand;
    }

    public void resetDeck() {
        deck.clear();
        deck.addAll(initializeDeck());
    }
}

// 게임 클래스
public class CardGameTest {
    private static final int NUMBER_OF_CARDS = 5;
    private static final int NUMBER_OF_ROUNDS = 100;
    private final List<Player> players;
    private final Dealer1 dealer1;

    public CardGameTest() {
        this.players = initializePlayers();
        this.dealer1 = new Dealer1();
    }

    public CardGameTest(List<Player> players, Dealer1 dealer1) {
        this.players = players;
        this.dealer1 = dealer1;
    }

    public static void main(String[] args) {
        CardGameTest game = new CardGameTest();
        game.start();
    }

    private List<Player> initializePlayers() {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();

        System.out.println("플레이어 수를 입력하세요 (최대 4명):");
        int playerCount;
        do {
            playerCount = scanner.nextInt();
            scanner.nextLine(); // 입력하기
            if (playerCount < 1 || playerCount > 4) {
                System.out.println("1에서 4 사이의 숫자를 입력하세요.");
            }
        } while (playerCount < 1 || playerCount > 4);

        for (int i = 0; i < playerCount; i++) {
            System.out.printf("플레이어 %d의 닉네임을 입력하세요: ", i + 1);
            String nickname = scanner.nextLine();
            players.add(new Player(nickname));
        }

        return players;
    }

    public void start() {
        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            playRound();
        }
        printFinalResults();
    }

    private void playRound() {
        dealer1.resetDeck();
        dealer1.shuffleDeck();
        for (Player player : players) {
            List<Card> hand = dealer1.dealCards(NUMBER_OF_CARDS);
            int roundScore = calculateHandScore(hand);
            player.addScore(roundScore);
        }
    }

    private int calculateHandScore(List<Card> hand) {
        return hand.stream().mapToInt(Card::getValue).sum();
    }

    private void printFinalResults() {
        players.sort(Comparator.comparingInt(Player::getTotalScore).reversed());
        System.out.println("최종 순위:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("%d위: %s (총 점수: %d)%n", i + 1, player.getNickname(), player.getTotalScore());
        }
    }
}
