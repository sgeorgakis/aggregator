FROM library/cassandra:latest

COPY init-database.cql /opt/
COPY init-database.sh /opt/

RUN ["chmod", "+x", "/opt/init-database.sh"]