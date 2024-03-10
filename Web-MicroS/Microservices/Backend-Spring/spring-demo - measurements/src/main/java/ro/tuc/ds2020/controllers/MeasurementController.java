package ro.tuc.ds2020.controllers;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.services.MeasurementService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping(value = "/measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @ResponseBody
    public List<Measurement> getAll() {
        return measurementService.getAll();
    }

//    @PutMapping("/updateMhecForDeviceSeven")
//    public ResponseEntity<Boolean> updateMhecForDeviceSeven(@RequestBody Measurement updatedMeasurement) {
//        boolean isUpdated = measurementService.updateMhecForDeviceSeven(updatedMeasurement);
//        if (isUpdated) {
//            return new ResponseEntity<>(true, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
//        }
//    }

}
