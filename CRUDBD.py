# pip install mysql-connector-python

#recuerda que debes crear una base de datos en mysql llamada pruebaperu
#create database pruebaperu;
#entrar a esa base de datos
#use pruebaperu;
#y dentro una tabla llamada datos
#create table datos(dato varchar(70));
#despues de esto, el programa debe funcionar

import os
import time
import mysql.connector
#debes descargar el conector con esta instrucción: pip install mysql.connector desde el cmd
#de windows o la terminal de tu sistema operativo

# Conexión a la base de datos
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="",
    database="pruebaperu"
)

mycursor = mydb.cursor()

while True:
    print('---MENU DEL SISTEMA---'
          '\n 1. Insertar un dato.'
          '\n 2. Eliminar un dato.'
          '\n 3. Buscar un dato.'
          '\n 4. Sobreescribir un dato.'
          '\n 5. Mostrar el contenido de la lista.'
          '\n 6. Salir del sistema.')
    opcion = int(input('Elige una opción: '))
    if opcion == 1:  # Create (insertar)
        dato = input('Ingresa el dato a insertar: ')
        sql = "INSERT INTO datos (dato) VALUES (%s)"
        val = (dato,)
        mycursor.execute(sql, val)
        mydb.commit()
        print(mycursor.rowcount, "registro insertado.")
        time.sleep(2)
        os.system('cls')
    elif opcion == 2:  # delete (eliminar)
        dato = input('Ingresa el dato a eliminar: ')
        sql = "DELETE FROM datos WHERE dato = %s"
        val = (dato,)
        mycursor.execute(sql, val)
        mydb.commit()
        print(mycursor.rowcount, "registro(s) eliminado(s).")
        time.sleep(2)
        os.system('cls')
    elif opcion == 3:  # Read (leer)
        dato = input('Ingresa el dato a buscar: ')
        sql = "SELECT * FROM datos WHERE dato = %s"
        val = (dato,)
        mycursor.execute(sql, val)
        myresult = mycursor.fetchall()
        if myresult:
            print('El dato está en la tabla.')
        else:
            print('El dato no existe en la tabla.')
        time.sleep(2)
        os.system('cls')
    elif opcion == 4:  # update (actualizar)
        datoAnterior = input('Ingresa el dato a sobreescribir: ')
        datoNuevo = input('Ingresa el nuevo dato: ')
        sql = "UPDATE datos SET dato = %s WHERE dato = %s"
        val = (datoNuevo, datoAnterior)
        mycursor.execute(sql, val)
        mydb.commit()
        print(mycursor.rowcount, "registro(s) actualizado(s).")
        time.sleep(2)
        os.system('cls')
    elif opcion == 5:  # Read (leer)
        mycursor.execute("SELECT * FROM datos")
        myresult = mycursor.fetchall()
        print("Contenido de la tabla:")
        for x in myresult:
            print(x)
        time.sleep(3)
        os.system('cls')
    elif opcion == 6:  # salir del sistema
        respuesta = input('Estás seguro? (s/n)')
        if respuesta.upper() == 'S':
            print('Saliendo del sistema...')
            time.sleep(2)
            os.system('cls')
            break
        time.sleep(2)
        os.system('cls')
    else:
        print('Opción no válida, intenta de nuevo...')
        time.sleep(2)
        os.system('cls')

# Cerrar la conexión
mydb.close()