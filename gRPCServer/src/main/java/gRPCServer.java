import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.grpc.server.UserInfoOuterClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class gRPCServer {
    private static final Logger log = Logger.getLogger(gRPCServer.class.getName());

    public static List<UserInfoOuterClass.USER> users = new ArrayList<UserInfoOuterClass.USER>();

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server grpcServer = ServerBuilder.forPort(port).addService(new UserInfoImpl())
                .build();
        grpcServer.start();
        log.info("Server started, Listing to : " + port);
        grpcServer.awaitTermination();
    }

    public static UserInfoOuterClass.USER getUser(String ID) {
        for (UserInfoOuterClass.USER user : gRPCServer.users) {
            if (user.getID().equals(ID)) {
                return user;
            }
        }
        return null;
    }
}
