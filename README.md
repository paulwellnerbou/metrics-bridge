# metrics-bridge

Application to transmit metrics (offered per JSON) to monitoring tools (Librato currently implemented).

---
*Currently still in heavy development, work in progress.*

---

## Configuration

See example and test files under src/test/resources as a first reference.

## Running

* Download the latest release via github and unzip it
* Run it:
```
./metrics-bridge-0.2-SNAPSHOT/bin/metrics-bridge -c <PATH TO YOUR CONFIG FILE>
```

Currently there is no daemon mode implemented, so you might want to run it with nohup:

	nohup ./metrics-bridge-0.2-SNAPSHOT/bin/metrics-bridge -c <PATH TO YOUR CONFIG FILE> &
