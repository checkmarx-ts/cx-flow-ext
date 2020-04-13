@REM Download and install the cxflow release
set /p CX_FLOW_VERSION=<cxflow.version
curl -L https://github.com/checkmarx-ts/cx-flow/releases/download/%CX_FLOW_VERSION%/cx-flow-%CX_FLOW_VERSION%.jar --output target\cx-flow-%CX_FLOW_VERSION%.jar