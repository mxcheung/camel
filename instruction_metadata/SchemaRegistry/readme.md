
🔑 Key changes

Added fallbackSchemas as a ConcurrentHashMap to store the last successfully fetched schema per subject.

On a successful fetch, the map is updated so that the fallback is always up to date.

On an exception, the service tries to retrieve the last known schema from the fallback map.

If no fallback exists, it throws your custom SchemaRegistryServiceException.
