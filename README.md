# musicBackend

Backend API for my music Project that doesn't have a name


Session code - code for everyone to view the queue of songs

```/createcode``` -> returns session code and admin code 

```/{code}/add/{query}``` -> adds a youtube video with the query to the database row with the code

```/{code}/delete/{adminCode}/{ytVideoCode}``` -> Delete video from queue,Only admin can delete from the queue

```/{code}``` -> lists the queue

```/{code}/clear/{adminCode}``` -> clears the queue

The middlewear is Spring Boot

The database is a H2 Database which means it runs in the memory to be faster but clears on termination.
