# Advent of Code Template

This project is a template for solving the Advent of Code challenges in Java. 
It uses Quarkus as the base framework and allows you to easily create new classes for each day's challenge, test for against the test input and finally execute the code against the real input automatically.

## Usage

1. Create a new class in the `src/main/java/org/sedam/aoc` package for the specific day you want to solve.

2. Implement the `part1` and `part2` methods in the class.

3. Copy the `dayX` folder already present serves as a template with the name of the day you want to solve and add paste the content where needed. Here is an explanation of the files:

    - `expected-result-1.txt`: The expected result for the test input of part 1.
    - `expected-result-2.txt`: The expected result for the test input of part 2.
    - `input-1.txt`: The real input for part 1.
    - `input-2.txt`: The real input for part 2.
    - `test_input-1.txt`: The test input for part 1.
    - `test_input-2.txt`: The test input for part 2.

4. Start the Quarkus application with `./mvn quarkus:dev -day=X`. Replace `X` with the day you want to solve.

**_NOTE:_**  You could also add an alias to your shell to make this easier. For example, in `~/.bashrc` or `~/.zshrc`:

```shell script
aoc() {
    mvn quarkus:dev -Dday=$1
}
```

Then you can start the application with `aoc X`.

Once the application is running, the tests will be re-executed automatically whenever you change the code.
Once the tests pass, the code will be executed against the real input and the result will be printed to the console.