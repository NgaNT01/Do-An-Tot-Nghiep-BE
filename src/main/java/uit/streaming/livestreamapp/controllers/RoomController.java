package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Room;
import uit.streaming.livestreamapp.services.RoomService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/all")
    public List<Room> getAllRoom() {
        return roomService.getAllRoom();
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<Room> getRoomByName(@PathVariable("roomName") String roomName) {
        return new ResponseEntity<Room>(roomService.getRoomByName(roomName), HttpStatus.OK);
    }

}
