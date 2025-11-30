# Advent of Code Java Runner


A reusable template and automation framework for solving Advent of Code puzzles in Java 25.

This project provides a lightweight CLI runner, automatic input fetching, test verification, and answer submission — all configurable from the command line or IDE.

Each day’s solution is isolated in its own DayXX class, with part‑1 and part‑2 logic managed automatically once implemented.

---

## Usage

### 1. Create a new day template:


	mvn compile exec:java -Dexec.mainClass=cli.AdventCLI -Dexec.args="new --day 3"

This generates:


- src/main/java/advent/Day03.java

- src/main/resources/day03/test-1.txt

- src/main/resources/day03/input-1.txt (fetched automatically from AoC)

You can also run it directly in IntelliJ using the AdventCLI main class.


---

### 2. Implement your solution


Edit the generated DayXX.java file and implement:


	@Override
	public String part1(String input) { ... }
	
	@Override
	public String part2(String input) { ... }

Run tests directly from your IDE or CLI.

Unimplemented parts should throw new NotImplementedException() until ready.


---

### 3. Run a day


	mvn compile exec:java -Dexec.mainClass=cli.AdventCLI -Dexec.args="run --day 3"


The runner will:


1. Load the test input and expected output.

2. Validate Part 1.

3. If successful, run real input and auto‑submit it.

4. If accepted on Advent of Code, automatically fetch input-2.txt and scaffold test-2.txt.

5. Continue with Part 2.


---

### 4. Configure your session


Before fetching or submitting, export your Advent of Code session token from your browser cookies:

	export AOC_SESSION="<your-session-cookie>"

This variable is required once per system; it will be reused automatically for all days.


---

### 5. Logs and Output


Logs are printed in structured, colored lines:


	=== Day03 ===
	[INFO]  Running Part 1 test ...
	[OK]    Submission accepted. Correct answer!
	[INFO]  Fetching input‑2 for day 3
	[INFO]  Saved input‑2 and test‑2 for day 3


---

## Optional Commands

| Command            | Description                                                |
|--------------------|------------------------------------------------------------|
| advent run --day N | Run the solution for day N                                 |
| advent new --day N | Scaffold a new class and resource folders                  |
| advent submit      | Manual answer submission (for debugging or manual control) |

You can run these through Maven’s exec:java goal or as pre‑configured run configurations in IntelliJ.


---

## Tips

- Each year can live in its own Git branch, keeping the master branch as a reusable framework.

- All logs, fetches, and submissions are handled automatically once your session token is configured.

- input‑1 and input‑2 are kept separate to prevent overwriting results between parts.


---

Example Run Flow

	=== Day01 ===
	[INFO]  Running Part 1 test ...
	[OK]    Part 1 (test) passed.
	[INFO]  Running Part 1 real input ...
	[OK]    Submission accepted. Correct answer!
	[INFO]  Fetching input‑2 for day 1 ...
	[INFO]  Saved input‑2 and test‑2 for day 1
	[INFO]  Running Part 2 test ...


---

## Requirements

- Java 25

- Maven ≥ 3.9

- AOC_SESSION environment variable set

No other dependencies or frameworks required.


---
