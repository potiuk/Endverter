import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import static groovy.io.FileType.FILES

private void updateSpeedsInTheSpeedZone(speedZone) {
    valueLow = speedZone.LowInMetersPerSecond.text()
    if (valueLow != '') {
        speedZone.LowInMetersPerSecond[0] = valueLow.toDouble() * 1.01
    }
    valueHigh = speedZone.HighInMetersPerSecond.text()
    if (valueHigh != '') {
        speedZone.HighInMetersPerSecond[0] = valueHigh.toDouble() * 0.99
    }
}

private void processWorkout(workout) {
    workout.Workouts.Workout.Step*.Target*.SpeedZone*.each { speedZone ->
        updateSpeedsInTheSpeedZone(speedZone)
    }

    workout.Workouts.Workout.Step*.Child*.Target*.SpeedZone*.each { speedZone ->
        updateSpeedsInTheSpeedZone(speedZone)
    }
}

private String serializeWorkout(workout) {
    result = XmlUtil.serialize(new StreamingMarkupBuilder().bind({
        mkp.declareNamespace('': workout.lookupNamespace(''))
        mkp.yield workout
    }))
}

private String processSingleFile(File inputFile) {
    def workout = new XmlSlurper().parse(inputFile);
    processWorkout(workout)
    result = serializeWorkout(workout)
    result
}

if (this.args.size() != 1) {
    println ("Specify file or directory as argument!")
    System.exit(1)
}
File inputFile = new File(this.args[0])
if (!inputFile.exists()) {
    println("The file ${inputFile} does not exit.")
    System.exit(2)
}
if (inputFile.isFile()) {
    result = processSingleFile(inputFile)
    println(result)
} else if (inputFile.isDirectory()) {
    convertedDir = new File(inputFile,'converted')
    convertedDir.mkdirs()
    inputFile.eachFileMatch(FILES, ~/.*\.tcx/, {file ->
        println("Processing ${file}")
        result = processSingleFile(file)
        new File(convertedDir, file.name).write(result)
    })
}





