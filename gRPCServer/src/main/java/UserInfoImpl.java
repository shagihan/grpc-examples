import org.example.grpc.server.UserInfoGrpc;
import org.example.grpc.server.UserInfoOuterClass;

public class UserInfoImpl extends UserInfoGrpc.UserInfoImplBase {
    public void addUser(org.example.grpc.server.UserInfoOuterClass.USER request,
                        io.grpc.stub.StreamObserver<org.example.grpc.server.UserInfoOuterClass.USER_REPLY> responseObserver) {
        UserInfoOuterClass.USER user = UserInfoOuterClass.USER.newBuilder()
                .setID(request.getID())
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setCivilStatus(request.getCivilStatus())
                .setTelephone(request.getTelephone())
                .setGenderStatus(request.getGenderStatus())
                .build();
        gRPCServer.users.add(user);
        responseObserver.onNext(UserInfoOuterClass.USER_REPLY.newBuilder().setMessage("SUCCESS")
                .setId(user.getID()).build());
        responseObserver.onCompleted();
    }

    /**
     *
     */
    public void getUser(org.example.grpc.server.UserInfoOuterClass.USER_ID request,
                        io.grpc.stub.StreamObserver<org.example.grpc.server.UserInfoOuterClass.USER> responseObserver) {
        UserInfoOuterClass.USER user = gRPCServer.getUser(request.getId());
        if(user != null) {
            responseObserver.onNext(user);
        } else {
            responseObserver.onNext(null);
        }
        responseObserver.onCompleted();
    }

    /**
     *
     */
    public void getUsers(org.example.grpc.server.UserInfoOuterClass.VOID_INPUT request,
                         io.grpc.stub.StreamObserver<org.example.grpc.server.UserInfoOuterClass.USERS_RESPONSE> responseObserver) {
        Iterable<UserInfoOuterClass.USER> users = gRPCServer.users;
        UserInfoOuterClass.USERS_RESPONSE users_response = UserInfoOuterClass.USERS_RESPONSE.newBuilder()
                .addAllUserInfo(users).build();
        responseObserver.onNext(users_response);
        responseObserver.onCompleted();
    }
}
