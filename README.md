# stock-quote-challenge

Implementing the stock quote web api

# Running the program
1. Make sure sbt is installed
2. Clone project into folder
3. To run unit tests, run `sbt test`
4. To run locally, run `sbt run`. Navigate browser to localhost:9000

#Things Missing/Todo
I decided that I should stop after a week and just send in what I had, unless you really want me to spend more time on it. But, these are the things I meant to add in but did not finish in time:
- Delete from watchlist is not implemented in UI
- Loading watchlist as group in api, instead of in UI. Right now the UI does the batch load of the quotes from data
- Allowing user to pick timed average data instead of just current quote

#Comments
I tried hard to use the preferred functional style, and encapsulating the stock requests, though the YahooFinance library made this difficult. I tried to put the entire requests into single calls to the service, which meant it had to wrap the static and Stock calls together. I also tried to handle all error states and exceptions.

The layers I used are the controller, for handling the request and result directly, parsing json, etc. Services, for performing data management and making business logic agnostic as to the interface type. And then the YahooFinance 'outside' layer. 

I put the YahooFinance static calls into a small wrapper to make it easier to mock in the unit tests