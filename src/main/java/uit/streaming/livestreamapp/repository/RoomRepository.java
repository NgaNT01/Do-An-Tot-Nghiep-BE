package uit.streaming.livestreamapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.streaming.livestreamapp.entity.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Room findRoomByRoomName(String roomName);

    List<Room> findAll();
}
