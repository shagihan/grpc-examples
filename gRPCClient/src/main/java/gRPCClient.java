import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.server.UserInfoGrpc;
import org.example.grpc.server.UserInfoOuterClass;

import java.util.Scanner;


public class gRPCClient {

    private static UserInfoGrpc.UserInfoBlockingStub userInfoBlockingStub;
    private static String userInfoPattern = "ID : %s \nName : %s\nMail : %s\nTelephone:%s\nStatus: %s\nGender: %s";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        gRPCClient.userInfoBlockingStub = UserInfoGrpc.newBlockingStub(channel);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select the operation, Press the operation key");
            System.out.println("1 : Add new user.");
            System.out.println("2 : List all users.");
            System.out.println("3 : Get a user by UID");
            int operation = Integer.parseInt(scanner.nextLine());
            switch (operation) {
                case 1:
                    addNewUser(scanner);
                    break;
                case 2:
                    listAllUsers();
                    break;
                case 3:
                    System.out.print("Enter UUID of User : ");
                    String uuid = scanner.nextLine();
                    getAUserByID(uuid);
                    break;
                default:
                    System.out.println("Invalid input, Please try again");
                    break;
            }
        }
    }

    private static void listAllUsers() {
        UserInfoOuterClass.USERS_RESPONSE response = userInfoBlockingStub
                .getUsers(UserInfoOuterClass.VOID_INPUT.newBuilder().build());
        for (UserInfoOuterClass.USER user : response.getUserInfoList()) {
            String stringResponse = String.format(userInfoPattern, user.getID(), user.getName(), user.getEmail(),
                    user.getTelephone(), user.getCivilStatus().toString(), user.getGenderStatus().toString());
            System.out.println("\n------------------------");
            System.out.println(stringResponse);
            System.out.println("------------------------\n");
        }
    }

    private static void addNewUser(Scanner scanner) {
        System.out.print("Enter UUID : ");
        String uuid = scanner.nextLine();
        System.out.print("Enter name : ");
        String name = scanner.nextLine();
        System.out.print("Enter mail : ");
        String mail = scanner.nextLine();
        System.out.print("Enter telephone : ");
        String telephone = scanner.nextLine();
        System.out.print("If single 1, Married 2 ");
        String status = scanner.nextLine();
        System.out.print("If Male 1, Female 2 ");
        String gender = scanner.nextLine();
        UserInfoOuterClass.USER user = UserInfoOuterClass.USER.newBuilder()
                .setID(uuid)
                .setName(name)
                .setEmail(mail)
                .setTelephone(telephone)
                .setCivilStatus((status.equals("1") ? UserInfoOuterClass.USER.status.SINGLE
                        : UserInfoOuterClass.USER.status.MARRIED))
                .setGenderStatus((gender.equals("1") ? UserInfoOuterClass.USER.gender.MALE
                        : UserInfoOuterClass.USER.gender.FEMALE))
                .build();
        UserInfoOuterClass.USER_REPLY response = userInfoBlockingStub.addUser(user);
        System.out.println("Status : " + response.getMessage());
    }

    private static void getAUserByID(String uid) {
        UserInfoOuterClass.USER_ID request = UserInfoOuterClass.USER_ID.newBuilder().setId(uid).build();
        UserInfoOuterClass.USER response = userInfoBlockingStub.getUser(request);
        String stringResponse = String.format(userInfoPattern, response.getID(), response.getName(),
                response.getEmail(), response.getTelephone(), response.getCivilStatus().toString(),
                response.getGenderStatus().toString());
        System.out.println("\n------------------------");
        System.out.println(stringResponse);
        System.out.println("------------------------\n");
    }
}
