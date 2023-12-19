# Task
Create a simple URL shortener service (UI is optional but will be considered a plus). Plan the implementation
to be suitable for very high volume of traffic and large amounts of data.
The service should be able to:

- Shorten a given URL and return shortened link;
- Expand a shortened URL into the original one and redirect to the original location.
  Shortened URLs should expire after a configured retention period.

### Implementation expectations
- Satisfactory implementation should consider aspects of the high volume and/or high load
- Security precautions should be taken for all shortened URLs (it is up to you to make a decisions what
  those security precautions are)
- Think how you can optimize the storage
- Consider how to scale your solution horizontally (it's not necessary to implement it, but prepare to
  explain this when you present your solution)
- [Optional] Think about monetization aspects of the service
### Language/framework to use
Any language/web framework. Live working demo is a plus, but not mandatory.
### Test coverage
You are expected to provide full unit test coverage for your implementation