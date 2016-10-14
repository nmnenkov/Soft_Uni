package com.nnenkov.mvh_sugarorm;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.nnenkov.mvh_sugarorm.model.MVHEvent;
import com.nnenkov.mvh_sugarorm.model.MVHEventTypes;
import com.nnenkov.mvh_sugarorm.model.MVHFuelTypes;
import com.nnenkov.mvh_sugarorm.model.MVHPart;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleBrands;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleModels;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleTypes;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by nik on 08.10.16.
 */

public class MVHApplication extends Application {
    private static final String TAG = Application.class.getName();
    private static final String DB_NAME = "mvhsugar.db";


    public List<MVHVehicleTypes> mMVHVehicleTypes;
    public List<MVHVehicleBrands> mMVHVehicleBrands;
    public List<MVHVehicleModels> mMVHVehicleModels;
    public List<MVHFuelTypes> mMVHFuelTypes;
    public List<MVHVehicle> mMVHVehicles;
    public List<MVHEventTypes> mMVHEventsTypes;
    public List<MVHEvent> mMVHEvents;
    public List<MVHPart> mMVHParts;


    @Override
    public void onCreate() {
        super.onCreate();


/*        if (doesDatabaseExist(this, DB_NAME.toString())) {
            SugarDb sugarDB = new SugarDb(getApplicationContext());
            new File(sugarDB.getDB().getPath()).delete();
        }*/


        SugarContext.init(getApplicationContext());

        boolean doesMVHDVExist = doesDatabaseExist(this, DB_NAME.toString());
        if (!doesMVHDVExist) {
            MVHVehicle.findById(MVHVehicle.class, (long) 1);
            MVHVehicleTypes.findById(MVHVehicleTypes.class, (long) 1);
            initDatabase();
        }

        mMVHVehicleTypes = MVHVehicleTypes.listAll(MVHVehicleTypes.class);
        mMVHVehicleBrands = MVHVehicleBrands.listAll(MVHVehicleBrands.class);
        mMVHVehicleModels = MVHVehicleModels.listAll(MVHVehicleModels.class);
        mMVHFuelTypes = MVHFuelTypes.listAll(MVHFuelTypes.class);
        mMVHVehicles = MVHVehicle.listAll(MVHVehicle.class);

        mMVHEvents = MVHEvent.listAll(MVHEvent.class);
        mMVHEventsTypes = MVHEventTypes.listAll(MVHEventTypes.class);

        mMVHParts = MVHPart.listAll(MVHPart.class);

        Log.d(TAG, String.valueOf(doesMVHDVExist));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    private boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void initDatabase() {

        MVHVehicleBrands opel = new MVHVehicleBrands("Opel", "OPEL");
        MVHVehicleBrands honda = new MVHVehicleBrands("Honda", "HONDA");
        MVHVehicleBrands ford = new MVHVehicleBrands("Ford", "FORD");
        opel.save();
        honda.save();
        ford.save();

        MVHVehicleModels opelAstra = new MVHVehicleModels(opel, "Astra", "ASTRA");
        MVHVehicleModels opelCalibra = new MVHVehicleModels(opel, "Calibra", "CALIBRA");
        MVHVehicleModels opelVectra = new MVHVehicleModels(opel, "Vectra", "VECTRA");

        MVHVehicleModels hondaCRV = new MVHVehicleModels(honda, "CRV", "CRV");
        MVHVehicleModels hondaCivic = new MVHVehicleModels(honda, "Civic", "CIVIC");
        MVHVehicleModels hondaCBR = new MVHVehicleModels(honda, "CBR", "CBR");

        MVHVehicleModels fordMustang = new MVHVehicleModels(ford, "Mustang", "MUSTANG");
        MVHVehicleModels fordEscord = new MVHVehicleModels(ford, "ESCORD", "ESCORD");

        opelAstra.save();
        opelCalibra.save();
        opelVectra.save();

        hondaCRV.save();
        hondaCivic.save();
        hondaCBR.save();

        fordMustang.save();
        fordEscord.save();


        MVHFuelTypes diesel = new MVHFuelTypes("Liquid", "Diesel");
        MVHFuelTypes gasoline = new MVHFuelTypes("Liquid", "Gasoline");
        MVHFuelTypes lpg = new MVHFuelTypes("Liquid", "LPG");

        diesel.save();
        gasoline.save();
        lpg.save();


        MVHVehicleTypes typeMotorbike = new MVHVehicleTypes("Motorbike", "motorbike_icon");
        MVHVehicleTypes typeCar = new MVHVehicleTypes("Car", "car_icon");
        MVHVehicleTypes typeTrack = new MVHVehicleTypes("Track", "track_icon");

        typeCar.save();
        typeMotorbike.save();
        typeTrack.save();

        MVHVehicle mMVHVehicle1 = new MVHVehicle(
                "HURBI 1",
                "CB 1111 AT",
                typeCar,
                honda,
                hondaCBR,
                "3.0D",
                0,
                diesel,
                52,
                diesel,
                40,
                1,
                "2016",
                new Date(),
                "60000",
                new Date());

        MVHVehicle mMVHVehicle2 = new MVHVehicle(
                "HURBI 2",
                "CB 1122 AT",
                typeMotorbike,
                opel,
                opelCalibra,
                "3.0D",
                0,
                gasoline,
                52,
                lpg,
                40,
                1,
                "2016",
                new Date(),
                "60000",
                new Date());

        MVHVehicle mMVHVehicle3 = new MVHVehicle(
                "HURBI 3",
                "CB 1133 AT",
                typeTrack,
                honda,
                hondaCBR,
                "3.0D",
                0,
                gasoline,
                52,
                lpg,
                40,
                1,
                "2016",
                new Date(),
                "60000",
                new Date());

        mMVHVehicle1.save();
        mMVHVehicle2.save();
        mMVHVehicle3.save();


        MVHEventTypes refuel = new MVHEventTypes("Refuel", "Frfuel fuel tank", "refuel_icon");
        MVHEventTypes replacedPart = new MVHEventTypes("Replaced Part", "Replaced Part", "part_replace_icon");
        MVHEventTypes insuranceGrajdancsa = new MVHEventTypes("3th party insurance", "3th party insurance", "insurance");
        MVHEventTypes insuranceCasko = new MVHEventTypes("Casko insurance", "Caso insurance", "insurance_casco");

        refuel.save();
        replacedPart.save();
        insuranceGrajdancsa.save();
        insuranceCasko.save();


        MVHPart fuelPump = new MVHPart("Fuel Pump","");
        MVHPart waterPump = new MVHPart("Water Pump","");
        MVHPart oilPump = new MVHPart("Oil Pump","");
        MVHPart coolingFan = new MVHPart("Cooling Fan","");
        MVHPart belt = new MVHPart("Belt","");

        fuelPump.save();
        waterPump.save();
        oilPump.save();
        coolingFan.save();
        belt.save();

        MVHEvent refuelEvent1 = new MVHEvent(mMVHVehicle1, 5, new Date(), "0.1", "0.1", "Nema", refuel, "100", "100", null, "nema Notes");
        refuelEvent1.save();
        MVHEvent refuelEvent2 = new MVHEvent(mMVHVehicle1, 23231, new Date(), "0.1", "0.1", "Nema", insuranceCasko, "100", "100", null, "nema Notes");
        refuelEvent2.save();
        MVHEvent refuelEvent3 = new MVHEvent(mMVHVehicle1, 54, new Date(), "0.1", "0.1", "Nema", insuranceGrajdancsa, "100", "100", null, "nema Notes");
        refuelEvent3.save();
        MVHEvent refuelEvent4 = new MVHEvent(mMVHVehicle1, 30, new Date(), "0.1", "0.1", "Nema", replacedPart, "100", "100", null, "nema Notes");
        refuelEvent4.save();
        for (int c = 1; c <= 30; c++) {
            MVHEvent refuelEvent = new MVHEvent(
                    mMVHVehicle1,
                    1000+c,
                    new Date(),
                    "0.1",
                    "0.1",
                    "Nema",
                    refuel,
                    "100",
                    "100",
                    null,
                    "nema Notes");

            refuelEvent.save();
        }

    }
}
