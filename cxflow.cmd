@REM Download and install the cxflow release
set /p CX_FLOW_VERSION=<cxflow.version
curl -L https://github.com/checkmarx-ts/cx-flow/releases/download/%CX_FLOW_VERSION%/cx-flow-%CX_FLOW_VERSION%.jar --output cx-flow-%CX_FLOW_VERSION%.jar
mvnw install:install-file -Dfile=cx-flow-%CX_FLOW_VERSION%.jar -DgroupId=com.checkmarx.flow -DartifactId=cx-flow -Dversion=%CX_FLOW_VERSION% -Dpackaging=jar -DgeneratePom=true
