#!/bin/bash


while [ $# -gt 0 ]; do
    arg=$1
    case $arg in
        -h|--help)
            help_text
        ;;
        -p|--profile)
            export AWS_DEFAULT_PROFILE="$2"
            shift; shift
        ;;
        -r|--report-bucket)
            REPORT_BUCKET="$2"
            shift; shift
        ;;
        -DUSERS)
          USERS="$2"
          shift;
          shift
        ;;
        -DRAMPUP)
            RAMPUP="$2"
            shift;
            shift
        ;;
        -DRAMPUP_DURATION)
            RAMPUP_DURATION="$2"
            shift;
            shift
        ;;
        -DDURATION)
            DURATION="$2"
            shift;
            shift
        ;;
        *)
            echo "ERROR: Unrecognised option: ${arg}"
            help_text
            exit 1
        ;;
    esac
done

if [ -z "$REPORT_BUCKET" ]
    then
        echo "Report bucket required. Please make sure its empty."
        help_text
        exit 1
fi

## Clean reports
rm -rf target/gatling/*

# Run load test
mvn gatling:test -Dgatling.simulationClass=simulations.ComputerDatabase -DUSERS=${USERS} -DRAMPUP=${RAMPUP} -DRAMPUP_DURATION=${RAMPUP_DURATION} -DDURATION=${DURATION}

#Upload reports
for _dir in target/gatling/*/
do
   aws s3 cp ${_dir}simulation.log s3://${REPORT_BUCKET}/logs/$HOSTNAME-simulation.log
done


# Example call:
#docker run gatling-runner -r test -DUSERS 5 -DRAMPUP 10 -DRAMPUP_DURATION 20 -DDURATION 30