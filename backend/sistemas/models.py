from django.db import models


class SistemaOperativo(models.Model):
    nombre = models.CharField(max_length=20, null=False, blank=False)
    version = models.CharField(null=False, blank=False,max_length=8)
    fechaLanzamiento = models.CharField(max_length=20, null=False, blank=False)
    peso_gigas = models.CharField(null=False, blank=False,max_length=5)


class Aplicacion(models.Model):
    nombre = models.CharField(max_length=20, null=False, blank=False)
    version = models.CharField(null=False, blank=False,max_length=8)
    fechaLanzamiento = models.CharField(max_length=20, null=False, blank=False)
    peso_gigas = models.CharField(null=False, blank=False,max_length=5)
    costo = models.DecimalField(null=False, blank=False,decimal_places=2,max_digits=5)
    url_descargar = models.CharField(max_length=150, null=False, blank=False)
    sistemaOperativo = models.ForeignKey(SistemaOperativo, on_delete=models.CASCADE)
