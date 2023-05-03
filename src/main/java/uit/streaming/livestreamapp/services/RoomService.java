package uit.streaming.livestreamapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.streaming.livestreamapp.entity.Room;
import uit.streaming.livestreamapp.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    public Room getRoomByName(String roomName) {
        return roomRepository.findRoomByRoomName(roomName);
    }
}
