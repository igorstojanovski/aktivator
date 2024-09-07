FROM postgres:16.4-bookworm
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password123
ENV POSTGRES_DB=causea
RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
ENV LANG en_US.utf8