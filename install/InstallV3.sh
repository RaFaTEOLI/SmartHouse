#!/bin/bash

# Script de Instalação e Configuração do Java, Tomcat, PostgreSQL, Backup, Firewall, Nmon, Htop

echo " _______  __   __  _______  ______    _______    __   __  _______  __   __  _______  _______ "
echo "|       ||  |_|  ||   _   ||    _ |  |       |  |  | |  ||       ||  | |  ||       ||       |"
echo "|  _____||       ||  |_|  ||   | ||  |_     _|  |  |_|  ||   _   ||  | |  ||  _____||    ___|"
echo "| |_____ |       ||       ||   |_||_   |   |    |       ||  | |  ||  |_|  || |_____ |   |___ "
echo "|_____  ||       ||       ||    __  |  |   |    |       ||  |_|  ||       ||_____  ||    ___|"
echo " _____| || ||_|| ||   _   ||   |  | |  |   |    |   _   ||       ||       | _____| ||   |___ "
echo "|_______||_|   |_||__| |__||___|  |_|  |___|    |__| |__||_______||_______||_______||_______|"
echo ""

echo "============================================================================================="
echo "=============================== Iniciando Script de Instalação =============================="
echo "============================================================================================="

function instalarJava() {
        echo "Instalando o Java..."
        sudo apt update
        sudo apt install default-jre default-jdk --assume-yes

        # Ajustando a versao do java
        cd /usr/local
        wget http://187.60.96.23/public/jdk-8u211-linux-x64.tar.gz
        tar -zxvf jdk-8u211-linux-x64.tar.gz
        update-alternatives --install /usr/bin/java java /usr/local/jdk1.8.0_211/bin/java 0

        echo "Ajustando a versao do Java..."
        echo "===== SELECIONE A OPCAO ONDE ESTEJA /usr/local/jdk1.8.0_211/bin/java ======="

        update-alternatives --config java

        echo "Versao do Java..."
        java -version
}

function instalarTomcat() {
        echo "Criando Usuario do Tomcat..."
        sudo mkdir /opt/tomcat
        sudo useradd -s /bin/false -d /opt/tomcat tomcat

        echo "Baixando o Tomcat..."
        cd /tmp
        wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.8/bin/apache-tomcat-9.0.8.tar.gz

        echo "Descompactando Arquivo..."
        sudo tar xzvf apache-tomcat-9.0.8.tar.gz -C /opt/tomcat --strip-components=1

        echo "Criando Link Simbolico da Versão..."
        sudo ln -s /opt/tomcat/apache-tomcat-9.0.8 /opt/tomcat/latest

        echo "Atualizando permissoes..."
        sudo chgrp -R tomcat /opt/tomcat
        cd /opt/tomcat/
        sudo chmod -R g+r conf
        sudo chmod g+x conf
        sudo chown -R tomcat webapps/ work/ temp/ logs/

        # Cria arquivo de serviço do tomcat
        echo "[Unit]
        Description=Apache Tomcat Web Application Container
        After=network.target

        [Service]
        Type=forking

        Environment=JAVA_HOME=/usr/lib/jvm/default-java
        Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
        Environment=CATALINA_HOME=/opt/tomcat
        Environment=CATALINA_BASE=/opt/tomcat
        Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
        Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'
        ExecStart=/opt/tomcat/bin/startup.sh
        ExecStop=/opt/tomcat/bin/shutdown.sh

        User=tomcat
        Group=tomcat
        UMask=0007
        RestartSec=10
        Restart=always

        [Install]
        WantedBy=multi-user.target" > /etc/systemd/system/tomcat.service

        sudo systemctl daemon-reload
        sudo systemctl start tomcat
        sudo systemctl status tomcat
        sudo systemctl enable tomcat

        echo "Liberando trafégo na porta 8080..."
        sudo ufw allow 8080/tcp

        PASS="smart_house"
        sed -i "s|password=\"<must-be-changed>\"|password=\"${PASS}\"|g" /opt/tomcat/conf/tomcat-users.xml

        # Reinicia o tomcat
        sudo systemctl restart tomcat
}

function instalarSSH() {
        echo "Instalando Servidor SSH..."
        apt-get install openssh-server --assume-yes

        # Substituti PermitRootLogin no para yes, para que possa acessar o SSH como root
        sed -i 's,PermitRootLogin no,PermitRootLogin yes,g' /etc/ssh/sshd_config
        sed -i 's,PermitRootLogin prohibit-password,PermitRootLogin yes,g' /etc/ssh/sshd_config
        sed -i 's,#PermitRootLogin prohibit-password,PermitRootLogin yes,g' /etc/ssh/sshd_config
        /etc/init.d/ssh stop
        /etc/init.d/ssh start
}

function instalarPostgreSQL() {
        echo "Instalando o PostgreSQL..." 
        apt-get install postgresql pgadmin3 --assume-yes

        # Altera a senha do usuário postgres
        echo "ALTER USER postgres WITH PASSWORD 'root';" | su -c "psql" - postgres

        # Alterando o arquivo de configuração do postgres para aceitar conexões de outras maquinas
        sed -i "s,#listen_addresses = 'localhost',listen_addresses = '*',g" /etc/postgresql/10/main/postgresql.conf

        # Adicionando os IPS de acesso ao BD
        echo "host    all         all           0.0.0.0/0           md5" >> /etc/postgresql/10/main/pg_hba.conf
        echo "host all all 192.168.0.0 255.255.255.0 md5" >> /etc/postgresql/10/main/pg_hba.conf

        # Reiniciando o postgres
        /etc/init.d/postgresql restart

        # Voltando ao diretorio ~
        cd ~

        echo "localhost:5432:smart_house:postgres:root" > .pgpass
        chmod 600 .pgpass

        echo "Configurando o Ambiente do Banco de Dados..."
        echo "CREATE DATABASE "smart_house"
        WITH OWNER = postgres
        ENCODING = 'UTF8'
        TABLESPACE = pg_default
        CONNECTION LIMIT = -1;" | sudo -i -u postgres psql
                
        echo "CREATE TABLE public.pessoa (
        pessoaId SERIAL,
        nome VARCHAR(35),
        sobrenome VARCHAR(35),
        usuario VARCHAR(35),
        senha VARCHAR(35),
        PRIMARY KEY (pessoaId)
        );

        INSERT INTO public.pessoa (nome, sobrenome, usuario, senha) VALUES ('Rafael', 'Tessarolo', 'rafael', '1234');

        CREATE TABLE public.casa (
        casaId SERIAL,
        proprietarioId INTEGER,
        nome VARCHAR(35),
        endereco VARCHAR(100),
        cidade VARCHAR(35),
        cep VARCHAR(12),
        FOREIGN KEY (proprietarioId) REFERENCES pessoa(pessoaId),
        PRIMARY KEY (casaID)
        );

        INSERT INTO public.casa (proprietarioId, nome, endereco, cidade, cep) VALUES (1, 'Casa da Praia', 'Av Banaguara', 'Fortaleza', '12345678');
        INSERT INTO public.casa (proprietarioId, nome, endereco, cidade, cep) VALUES (1, 'Casa de Valinhos', 'Estrada Municipal', 'Valinhos', '12312455');

        CREATE TABLE public.morador (
        moradorId SERIAL,
        pessoaId INTEGER,
        casaId INTEGER,
        data_cadastro DATE,
        FOREIGN KEY (pessoaId) REFERENCES pessoa(pessoaId) ON DELETE CASCADE,
        FOREIGN KEY (casaId) REFERENCES casa(casaId) ON DELETE CASCADE,
        PRIMARY KEY (moradorId)
        );

        INSERT INTO public.morador (pessoaId, casaId, data_cadastro) VALUES (1, 1, '2019-08-21');

        CREATE TABLE public.comodo (
        comodoId SERIAL,
        casaId INTEGER,
        nome VARCHAR(35),
        andar INTEGER,
        FOREIGN KEY (casaId) REFERENCES casa(casaId) ON DELETE CASCADE,
        PRIMARY KEY (comodoId)
        );

        INSERT INTO public.comodo (casaId, nome, andar) VALUES (1, 'Sala de TV', 1);
        INSERT INTO public.comodo (casaId, nome, andar) VALUES (1, 'Cozinha', 1);

        CREATE TABLE public.aparelho (
        aparelhoId SERIAL,
        comodoId INTEGER,
        nome VARCHAR(35),
        descricao VARCHAR(35),
        FOREIGN KEY (comodoId) REFERENCES comodo(comodoId) ON DELETE CASCADE,
        PRIMARY KEY (aparelhoId)
        );

        INSERT INTO public.aparelho (comodoId, nome, descricao) VALUES (1, 'TV 42', 'TV da sala');
        INSERT INTO public.aparelho (comodoId, nome, descricao) VALUES (1, 'Lampada LED', 'Lampada principal da Sala');
        INSERT INTO public.aparelho (comodoId, nome, descricao) VALUES (2, 'TV 32', 'TV da cozinha');
        " > /home/pgScript.sql

        echo "\i /home/pgScript.sql" | sudo -i -u postgres psql smart_house
}

function criarScriptBackup() {
        echo "Criando Script de Backup..."
        mkdir /etc/backup
        echo '#!/bin/bash

        pg_dump -h localhost -U postgres -F p smart_house > /etc/backup/backup.sql' > /etc/backup/backup.sh

        chmod +x /etc/backup/backup.sh

        # Sera feito backup todo dia meia noite
        echo "Criando Rotina de Backup..."
        /bin/echo >> mycron
        /bin/echo "00 00 * * * /etc/backup/backup.sh" >> mycron
}

function criarScriptMonitoramentoProcessos() {
        # Criando o Script de Monitoramento de Processos e configurando
        echo "Criando Ferramenta de Monitoramento de Processos..."
        mkdir /etc/logDeMemoria
        cd /etc/logDeMemoria
        echo '#!/bin/bash
        # Atribui a data atual para variavel data
        DATA=$(date "+%d-%m-%Y %H:%M:%S")

        # Cria a escrita do LOG
        LOG="LOG DE MEMÓRIA | $DATA"

        # Escreve os processos do servidor num arquivo txt
        ps -ef > /etc/logDeMemoria/processes.txt

        # Conta quantos processos tem e salva na variavel NUMOP
        NUMOP=$(wc -l < /etc/logDeMemoria/processes.txt)

        # Remove um processo pois ele conta a primeira linha que nao é um processo
        NUMOP=$(expr $NUMOP - 1)

        echo "Numero de processos: $NUMOP"

        # Se a quantidade de processos for maior que 50...
        if [[ $NUMOP -gt 50 ]]
        then
                echo "$LOG | CRÍTICO | Quantidade de Processos: $NUMOP" >> /etc/logDeMemoria/memory.log

        # Se a quantidade de processos for maior que 30 e menor que 50...
        elif [[ $NUMOP -gt 30 && $NUMOP -lt 50 ]]
        then
                echo "$LOG |  AVISO  | Quantidade de Processos: $NUMOP" >> /etc/logDeMemoria/memory.log
        else
                echo "$LOG |  INFO   | Quantidade de Processos: $NUMOP" >> /etc/logDeMemoria/memory.log
        fi
        ' > /etc/logDeMemoria/monitorarProcessos.sh
        chmod +x /etc/logDeMemoria/monitorarProcessos.sh
}

function criarScriptDeUsoMemoria() {
        # Criando o Script de Monitoramento de Uso de memória e configurando
        echo "Criando Ferramenta de Monitoramento de Uso de Memória..."
        mkdir /etc/usoDeMemoria/
        cd /etc/usoDeMemoria/
        echo '#!/bin/bash

        free -m > /etc/usoDeMemoria/usoMemoria.txt

        function traduzRetorno() {
                FROM=$1
                TO=$2
                echo "Alterando de $FROM para $TO"
                sed -i "s|$FROM|$TO|g" /etc/usoDeMemoria/usoMemoria.txt
        }

        traduzRetorno total Total
        traduzRetorno used Usado
        traduzRetorno free Livre
        traduzRetorno shared Compartilhada
        traduzRetorno available Disponivel
        traduzRetorno buff/cache cache
        ' > /etc/usoDeMemoria/monitorarUsoMem.sh
        chmod +x /etc/usoDeMemoria/monitorarUsoMem.sh
}

function criarScriptLogSmartHouse() {
        mkdir /etc/logSystem
        echo '#!/bin/bash
grep "LOG SYSTEM" /opt/tomcat/logs/catalina.out > /var/log/smart_house.log' > /etc/logSystem/logSystem.sh
}

function adicionarCrontab() {
        cd ~
        echo "Adicionando Monitorador de Processos no crontab..."
        # Executa script a cada 3 minutos
        /bin/echo "*/3 * * * * /etc/logDeMemoria/monitorarProcessos.sh" >> mycron
        /bin/echo "*/3 * * * * /etc/usoDeMemoria/monitorarUsoMem.sh" >> mycron
        /bin/echo "*/3 * * * * /etc/logSystem/logSystem.sh" >> mycron
        crontab mycron
}

function instalarProgramas() {
        # Instalando NMON, HTOP e IPTRAF
        echo "Instalando NMON..."
        apt-get install nmon --assume-yes

        echo "Instalando HTOP..."
        apt-get install htop --assume-yes

        echo "Instalando IPTRAF..."
        apt-get install iptraf --assume-yes

        echo "Configurando Firewall/IPTables..."
        apt-get install -y iptables-persistent
}

function instalarSmartHouse() {
        echo "Instalando Smart House Web..."
        apt-get install -y git
        cd /etc/
        mv Smart_House_Web/ /opt/tomcat/webapps/ROOT/Smart_House_Web
        mv /opt/tomcat/webapps/ROOT/Smart_House_Web/dist/Smart_House_Web.war /opt/tomcat/webapps/
        #http://localhost:8080/Smart_House_Web/
}

function configurarFirewall() {
        # ---------------------------- #
        #     Firewall Iptables        #
        # ---------------------------- #

        # IP do Servidor
        IP="$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1')"

        # Interface da Internet
        EXTERNA="eth0"

        # Limpa regras --------- |
        iptables -F
        iptables -t nat -F
        iptables -t mangle -F

        # Roteamento de kernel ---------------- |
        echo 1 > /proc/sys/net/ipv4/ip_forward

        # Compartilhar Internet --------------------------------------- |
        iptables -t nat -A POSTROUTING -o $EXTERNA -j MASQUERADE

        # Fazendo liberacao dos IPs -------------------------------- |
        echo "Liberando acesso ao IP que irá acessar o servidor SSH..."
        iptables -A INPUT -p tcp --dport 22 -s 192.168.1.36 -j ACCEPT

        echo "Liberando acesso ao IP que irá acessar o banco de dados..."
        iptables -A INPUT -p tcp --dport 5432 -s 192.168.1.36 -j ACCEPT

        echo "Liberando acesso ao IP que irá acessar a aplicacao..."
        iptables -A INPUT -p tcp --dport 8080 -s 192.168.1.36 -j ACCEPT

        echo "Liberando acesso ao IP do administrador do servidor..."
        iptables -I INPUT -s 192.168.1.40 -j ACCEPT
        iptables -I INPUT -s 192.168.1.36 -j ACCEPT

        echo "Liberando acesso total ao IP da maquina..." 
        iptables -I INPUT -s $IP -j ACCEPT

        # Otimiza conexão de entrada e saida ------------------------------- |
        iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
        iptables -A FORWARD -m state --state ESTABLISHED,RELATED -j ACCEPT
        iptables -A OUTPUT -m state --state RELATED,ESTABLISHED -j ACCEPT

        # Logs de bloqueios -------- |
        iptables -A INPUT -j LOG
        iptables -A OUTPUT -j LOG
        iptables -A FORWARD -j LOG

        # ping ----------------------------- |
        # Bloqueia o ping vindo de fora ---- |
        echo "Bloqueando o ping vindo de fora..."
        iptables -A INPUT -p icmp -j DROP
        iptables -A OUTPUT -p icmp -j ACCEPT

        # SSH ------------------------------------------------------- |
        iptables -A INPUT -p tcp --dport 22 -i $EXTERNA -j ACCEPT
        iptables -A OUTPUT -p tcp --sport 22 -o $EXTERNA -j ACCEPT

        # dns - resolve externo ---------------------------------------- |
        echo "Criando regras de DNS..."
        iptables -A INPUT -p udp --sport 53 -s 192.168.1.0/24 -j ACCEPT
        iptables -A OUTPUT -p udp --dport 53 -d 192.168.1.0/24 -j ACCEPT
        iptables -A OUTPUT -p udp --dport 53 -j ACCEPT

        # Bloqueia Tudo ------------ |
        echo "Bloqueiando tudo..."
        iptables -P INPUT DROP

        #echo "Bloqueando acesso externo ao servidor para todos IPs..."
        #iptables -A INPUT -s 0.0.0.0/0 -j DROP
        #echo "Bloqueando acesso a porta do SSH"
        #iptables -A INPUT -p tcp --dport 22 -j DROP
        #echo "Bloqueando acesso a porta do tomcat"
        #iptables -A INPUT -p tcp --dport 8080 -j DROP
        #echo "Bloqueando acesso a porta do PostgreSQL"
        #iptables -A INPUT -p tcp --dport 8080 -j DROP

        echo "Adicionando DNS do google no resolv.conf..."
        echo "nameserver 8.8.8.8" >> /etc/resolv.conf

        echo "Reiniciando Networking..."
        /etc/init.d/networking restart

        mkdir /etc/iptables

        echo "Salvando arquivo do Firewall..."
        iptables-save > /etc/iptables/iptables-save
}

function instalarPowerCPUTask() {
        echo "Instalando PowerCPUTask..."
        echo "Desenvolvido por: Rafael Tessarolo..."
        mkdir /etc/powerCPUTask/
        mkdir /etc/powerCPUTask/pid
        echo '#!/bin/bash
        
### BEGIN PROGRAM INFO
# Author: Rafael Tessarolo
# Name: powerCPUTask
# Description: Verifies a Process Using Too Much CPU and Changes its Priority.
### END PROGRAM INFO

NOMEPROGRAMA="powerCPUTask"
DATA=$(date "+%d-%m-%Y %H:%M:%S")
TIME=$(date "+%H:%M")
LOG="| $DATA | $NOMEPROGRAMA | LOG | "
PID=""
PTIME=""
NICE=""
PRI=""

function verificarProcessoAlto() {
        echo "$LOG Verificando processo utilizando mais CPU..."
        PID=$(ps aux | sort -k 4 -r | head -n 2 | awk ASPASEND{print $2}ASPAS)
}

function datediff() {
    d1=$(date -d "$1" +%s)
    d2=$(date -d "$2" +%s)
    echo $(( (d1 - d2) / 86400 ))
}

function verificarTempoProcesso() {
        echo "$LOG Verificando tempo do processo (PID: $PID) utilizando mais CPU..."
        PTIME=$(ps aux | sort -k 4 -r | head -n 2 | awk ASPASEND{print $9}ASPAS)

        echo "$LOG Tempo encontrado do processo (PID: $PID): $PTIME"
        DIFF=$(datediff $TIME $PTIME)
        if [[ $DIFF > 86400‬ ]]
        then
                echo "$LOG Tempo de processo muito alto (PTIME: $PTIME)"
                PRI="MUITO ALTA"
                NICE="-20"
        else
                echo "$LOG Tempo de processo normal (PTIME: $PTIME)"
                PRI="ALTA"
                NICE="-10"
        fi
}

function priorizarProcesso() {
        PID=$1
        echo "$LOG Priorizando processo (PID: $PID) com prioridade $PRI..."
        renice $NICE -p $PID
}

function main() {
        while [[ true ]]; do
                echo "$LOG Iniciando Programa..."
                verificarProcessoAlto
                verificarTempoProcesso
                priorizarProcesso $PID
                echo "$LOG Finalizando Programa..."
                sleep 60m
        done
}

main
' > /etc/powerCPUTask/powerCPUTask.sh
        chmod +x /etc/powerCPUTask/powerCPUTask.sh
        sed -i "s|ASPAS|'|g" /etc/powerCPUTask/powerCPUTask.sh
        echo "PowerCPUTask.sh instalado!"

        echo '#!/bin/bash
### BEGIN INIT INFO
# Author: Rafael Tessarolo
# Required-Start: $all
# Required-Stop: $local_fs
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Description: Verifies Process Using Too Much CPU.
### END INIT INFO

NOMEPROGRAMA="powerCPUTask"

DAEMON_PATH="/etc/$NOMEPROGRAMA"
DAEMON="$DAEMON_PATH/$NOMEPROGRAMA.sh"
NAME="$NOMEPROGRAMA"
LOGFILE="/var/log/$NAME.log"
DESC="$NOMEPROGRAMA Daemon"
PIDFILE=$DAEMON_PATH/pid/$NAME.pid
SCRIPTNAME=/etc/init.d/$NAME
RUNTIME="$2"

function start() {
   printf "%-50s" "Starting $NAME..."
   PID=`$DAEMON >> $LOGFILE 2>&1 & echo $!`
   if [ -z $PID ]; then
        printf "%s\n" "Fail create PID File"
   else
        echo $PID > $PIDFILE
        printf "%s\n" "Create PID File"
    fi
}

function status() {
    printf "%-50s" "Checking $NAME..."
    if [ -f $PIDFILE ]; then
        PID=`cat $PIDFILE`
        if [ -z "`ps axf | grep ${PID} | grep -v grep`" ]; then
            printf "%s\n" "Process dead but pidfile exists"
        else
            echo "Running"
        fi
    else
        printf "%s\n" "Service not running"
    fi
}

function stop() {
    printf "%-50s" "Stopping $NAME"
        PID=`cat $PIDFILE`
        cd $DAEMON_PATH
    if [ -f $PIDFILE ]; then
        kill -9 $PID
        printf "%s\n" "Ok"
        rm -f $PIDFILE
    else
        printf "%s\n" "pidfile not found"
    fi
}

function runtime() {
    printf "%-50s" "Starting $NAME and modifying runtime..."
    echo $RUNTIME
    PID=`$DAEMON $RUNTIME >> $LOGFILE 2>&1 & echo $!`
    if [ -z $PID ]; then
        printf "%s\n" "Fail"
    else
        echo $PID > $PIDFILE
        printf "%s\n" "Ok"
    fi
}

case "$1" in	
start)
start
;;
status)
status
;;	
stop)
stop
;;
restart)
stop
start
;;	
runtime)
if [[ -z $2 ]]; then
        echo "Invalid argument"
else
        stop
        runtime
fi
;;	
log|stdout)
if [ -f $LOGFILE ]; then
        tail -f -n 30 $LOGFILE
else
        echo "No log output yet"
fi
;;
help|?|--help|-h)
echo "Usage: $0 {status|start|stop|restart|runtime|(log|stdout)}"
exit 0
;;
*)
echo "Invalid argument"
echo
$0 help
esac
' > /etc/init.d/powerCPUTask
        chmod +x /etc/init.d/powerCPUTask
        echo "PowerCPUTask init instalado!"
}

function principal() {
        instalarJava
        instalarTomcat
        instalarSSH
        instalarPostgreSQL
        criarScriptBackup
        criarScriptMonitoramentoProcessos
        criarScriptDeUsoMemoria
        adicionarCrontab
        instalarProgramas
        instalarSmartHouse
        configurarFirewall
        instalarPowerCPUTask
}

principal

echo "============================================================================================="
echo "============================== Script de Instalação Finalizado =============================="
echo "============================================================================================="