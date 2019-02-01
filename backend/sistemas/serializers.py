from rest_framework import serializers
from .models import SistemaOperativo


class SistemaOperativoSerializado(serializers.ModelSerializer):
    class Meta:
        model = SistemaOperativo
        fields = '__all__'