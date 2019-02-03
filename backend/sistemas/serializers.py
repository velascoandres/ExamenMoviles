from rest_framework import serializers
from .models import *


class SistemaOperativoSerializado(serializers.ModelSerializer):
    class Meta:
        model = SistemaOperativo
        fields = '__all__'


class AplicacionSerializada(serializers.ModelSerializer):
    class Meta:
        model = Aplicacion
        fields = '__all__'
