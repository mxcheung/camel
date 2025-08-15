
```
[ Route processes Exchange ]
         |
         v
[ An Exception is thrown ]
         |
         v
  ┌───────────────────────-───┐
  │ Does any onException(...) │
  │ match this Exception type?│
  └────────────────────────-──┘
        YES | NO
            |  
YES ────────┘  
            v
     NO → [ Apply default/errorHandler(...) settings ]
            |
            v
  Retry? → YES → [ Redeliver after delay, go back to route step ]
            |
            NO
            |
            v
   [ Mark Exchange as failed, send to Dead Letter Channel if configured ]
```

# Example Visual
```
                  +----------------+
                  | Exception      |
                  +----------------+
                           |
         +-----------------+-----------------+
         |                                   |
  Matches onException?               No match
         |                                   |
   Apply onException rules           Apply defaultErrorHandler rules
 (retry, fallback, handled, etc.)   (retry, delay, dead letter, etc.)

```
